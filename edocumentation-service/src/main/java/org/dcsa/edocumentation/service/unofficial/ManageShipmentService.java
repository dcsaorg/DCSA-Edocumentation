package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.EquipmentAssignment;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.ShipmentLocationService;
import org.dcsa.edocumentation.service.ShipmentTransportService;
import org.dcsa.edocumentation.service.mapping.EquipmentAssignmentMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;
import org.dcsa.edocumentation.transferobjects.unofficial.EquipmentAssignmentTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ManageShipmentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.repository.CarrierRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManageShipmentService {
  private final ShipmentRepository shipmentRepository;
  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;
  private final ShipmentMapper shipmentMapper;
  private final CarrierRepository carrierRepository;
  private final EquipmentAssignmentMapper equipmentAssignmentMapper;
  private final EquipmentRepository equipmentRepository;
  private final BookingValidationService bookingValidationService;
  private final ShipmentLocationService shipmentLocationService;
  private final ShipmentTransportService shipmentTransportService;

  private static final Predicate<ShipmentLocationTypeCode> HAS_SOURCE_LOCATION = code ->
          code == ShipmentLocationTypeCode.PRE || code == ShipmentLocationTypeCode.POL;
  private static final Predicate<ShipmentLocationTypeCode> HAS_DESTINATION_LOCATION = code ->
          code == ShipmentLocationTypeCode.POD || code == ShipmentLocationTypeCode.PDE;
  public static final Predicate<ShipmentLocationTO> HAS_SOURCE_OR_DESTINATION_LOCATION =
          sl -> HAS_SOURCE_LOCATION.test(sl.shipmentLocationTypeCode())
                  || HAS_DESTINATION_LOCATION.test(sl.shipmentLocationTypeCode());

  public static final List<ShipmentLocationTypeCode> SHIPMENT_LOCATION_TYPE_CODE_ORDER =
          List.of(ShipmentLocationTypeCode.PRE, ShipmentLocationTypeCode.POL, ShipmentLocationTypeCode.POD, ShipmentLocationTypeCode.PDE);


  @Transactional
  public ShipmentRefStatusTO create(ManageShipmentRequestTO shipmentRequestTO) {
    Booking booking = getBooking(shipmentRequestTO);
    Carrier carrier = carrierRepository.findBySmdgCode(shipmentRequestTO.carrierSMDGCode());
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("Unrecognized SMDG LCL party code \""
        + shipmentRequestTO.carrierSMDGCode() + "\". Note the code may be valid but not loaded into this system.");
    }
    OffsetDateTime confirmationTime = OffsetDateTime.now();
    ValidationResult<BkgDocumentStatus> validationResult = bookingValidationService.validateBooking(booking);

    shipmentEventRepository.save(booking.confirm(confirmationTime));

    // FIXME: Check if the shipment (CBR) already exists and then attempt to reconfirm it if possible rather than
    //  die with a 409 Conflict (or create a duplicate or whatever happens). Probably just set "valid_until = now()"
    //  on the old shipment if it exists and then create a new one.
    //  On reconfirm: If a CBR is not provided, then keep the original CBR. Otherwise, if they are distinct, replace
    //  the old one with the new.
    Shipment shipment = Shipment.builder()
            // FIXME: The booking must be deeply cloned, so the shipment is not mutable via PUT /bookings/{...}
            .booking(booking)
            .carrier(carrier)
            .carrierBookingReference(shipmentRequestTO.carrierBookingReference())
            .shipmentCreatedDateTime(confirmationTime)
            .shipmentUpdatedDateTime(confirmationTime)
            .termsAndConditions(shipmentRequestTO.termsAndConditions())
            .build();

    if (!validationResult.validationErrors().isEmpty()) {
      return shipmentMapper.toStatusDTO(shipment, validationResult.proposedStatus());
    }

    // FIXME: This should be embedded into `validationResult`.
    validateTransportPlans(shipmentRequestTO, shipmentRequestTO.shipmentLocations());

    shipment.assignEquipments(
      Objects.requireNonNullElse(
        shipmentRequestTO.equipmentAssignments(),
        Collections.<EquipmentAssignmentTO>emptyList()
      ).stream()
        .map(this::mapEquipmentAssignment)
        .toList()
    );


    shipment = shipmentRepository.save(shipment);
    shipmentLocationService.createShipmentLocations(shipmentRequestTO.shipmentLocations(), shipment);
    shipmentTransportService.createShipmentTransports(shipmentRequestTO.transports(), shipment);
    return shipmentMapper.toStatusDTO(shipment, booking.getDocumentStatus());
  }

  private Boolean validateTransportPlans(ManageShipmentRequestTO manageShipmentRequestTO, List<ShipmentLocationTO> shipmentLocationTOS) {
    if (manageShipmentRequestTO.transports().isEmpty()) {
      throw ConcreteRequestErrorMessageException.conflict("Transport Plans is empty; cannot be validated", null);
    }
    var transportToList = manageShipmentRequestTO.transports().stream()
            .sorted(Comparator.comparingInt(TransportTO::transportPlanStageSequenceNumber))
            .collect(Collectors.toList());

    for (int i = 0; i < transportToList.size() - 1; i++) {
      LocationTO currentDischargeLocation = transportToList.get(i).dischargeLocation();
      LocationTO nextLoadLocation = transportToList.get(i + 1).loadLocation();

      if (currentDischargeLocation.UNLocationCode() != null && nextLoadLocation.UNLocationCode() != null) {
        if(!currentDischargeLocation.UNLocationCode().equals(nextLoadLocation.UNLocationCode()))
        {
          throw ConcreteRequestErrorMessageException.invalidInput("TransportPlan should be connected, " +
                  "such that the loadLocation of a subsequent leg must be the same as the dischargeLocation ");
        }
      } else {
        if (!currentDischargeLocation.address().equals(nextLoadLocation.address())) {
          throw ConcreteRequestErrorMessageException.invalidInput("TransportPlan should be connected, " +
                  "such that the loadLocation of a subsequent leg must be the same as the dischargeLocation ");
        }
      }
    }

    List<ShipmentLocationTO> filteredshipmentLocationTOS = shipmentLocationTOS.stream()
            .filter(HAS_SOURCE_OR_DESTINATION_LOCATION)
            .toList();

    boolean AllShipmentLocationTypeCodeExist = filteredshipmentLocationTOS.stream()
            .allMatch(sl -> transportToList.stream()
                    .anyMatch(tp ->
                            (sl.location().address() != null &&
                                    (sl.location().address().equals(tp.loadLocation().address()) ||
                                            sl.location().address().equals(tp.dischargeLocation().address())))
                                    ||
                                    (sl.location().UNLocationCode() != null &&
                                            (sl.location().UNLocationCode().equals(tp.loadLocation().UNLocationCode()) ||
                                                    sl.location().UNLocationCode().equals(tp.dischargeLocation().UNLocationCode())))
                    ));

    if(!AllShipmentLocationTypeCodeExist){
      throw ConcreteRequestErrorMessageException.invalidInput("All Shipment locations with codes: PRE, POL, POD, PDE. Must appear in " +
              "Transport locations.");
    }

    // Find the first occurrence of each shipment location in the transport plan and index it.
    Map<ShipmentLocationTypeCode,Integer> firstOccurrences = new HashMap<>();
    for (int i = 0; i < transportToList.size(); i++) {
      TransportTO leg = transportToList.get(i);

      ShipmentLocationTO loadShipmentLocation = filteredshipmentLocationTOS.stream()
              .filter(sl ->
                      (sl.location().address() != null &&
                      sl.location().address().equals(leg.loadLocation().address()))
                      ||   (sl.location().UNLocationCode() != null &&
                              sl.location().UNLocationCode().equals(leg.loadLocation().UNLocationCode())))
              .findFirst()
              .orElse(null);

      ShipmentLocationTO dischargeShipmentLocation = filteredshipmentLocationTOS.stream()
              .filter(sl -> Objects.equals(sl.location().address(), leg.dischargeLocation().address())
                      || Objects.equals(sl.location().UNLocationCode(), leg.dischargeLocation().UNLocationCode()))
              .findFirst()
              .orElse(null);


      // REPETIONS ARE NOT INCLUDED
      if (loadShipmentLocation != null && !firstOccurrences.containsKey(loadShipmentLocation.shipmentLocationTypeCode())) {
        firstOccurrences.put(loadShipmentLocation.shipmentLocationTypeCode(), i);
      }

      if (dischargeShipmentLocation != null && !firstOccurrences.containsKey(dischargeShipmentLocation.shipmentLocationTypeCode())) {
        firstOccurrences.put(dischargeShipmentLocation.shipmentLocationTypeCode(), i);
      }
    }

    // Check if the first and last locations are PRE and PDE or POL and POD respectively
    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PRE) && firstOccurrences.get(ShipmentLocationTypeCode.PRE) != 0){
      throw ConcreteRequestErrorMessageException.invalidInput("PRE must be the first location if provided.");
    }

    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PDE) && firstOccurrences.get(ShipmentLocationTypeCode.PDE) != transportToList.size() - 1){
      throw ConcreteRequestErrorMessageException.invalidInput("PDE must be the last location if provided.");
    }
    // Check if PDE and POD, if present, are at the correct positions;
    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PDE)){
      if (firstOccurrences.get(ShipmentLocationTypeCode.PDE) != transportToList.size() - 1) {
        throw  ConcreteRequestErrorMessageException.invalidInput("PDE must be the last location if provided");
      }
    } else if (firstOccurrences.containsKey(ShipmentLocationTypeCode.POD)) {
      if (firstOccurrences.get(ShipmentLocationTypeCode.POD) != transportToList.size() - 1)
        throw  ConcreteRequestErrorMessageException.invalidInput("POD must be the last location if PDE is not provided.");
      }

      // Check that the first occurrences follow the expected order
      for (int i = 0; i < SHIPMENT_LOCATION_TYPE_CODE_ORDER.size() - 1; i++) {
        ShipmentLocationTypeCode current = SHIPMENT_LOCATION_TYPE_CODE_ORDER.get(i);
        ShipmentLocationTypeCode next = SHIPMENT_LOCATION_TYPE_CODE_ORDER.get(i + 1);

        if (firstOccurrences.containsKey(current) && firstOccurrences.containsKey(next) &&
                firstOccurrences.get(current) > firstOccurrences.get(next)) {
          throw ConcreteRequestErrorMessageException.invalidInput("Transport locations must follow the Shipment locations" +
                  " order: PRE, POL, POD, PDE.");
        }
    }

    // CHECK FOR: the minimum required length
    long countOfPreAndPde = shipmentLocationTOS.stream()
            .filter(sl -> sl.shipmentLocationTypeCode().equals(ShipmentLocationTypeCode.PRE)
                    || sl.shipmentLocationTypeCode().equals(ShipmentLocationTypeCode.PDE))
            .count();

    if (countOfPreAndPde >= transportToList.size()) {
      throw ConcreteRequestErrorMessageException.invalidInput("Transport plan does not meet the minimum required length.");
    }
    validateTransportStages(transportToList, shipmentLocationTOS, firstOccurrences);
    validateModeOfTransport(transportToList, firstOccurrences);
     return true;
  }

  void validateTransportStages(List<TransportTO> transportToList, List<ShipmentLocationTO> shipmentLocationTOS, Map<ShipmentLocationTypeCode, Integer> firstOccurrences) {

    var SlCodes = shipmentLocationTOS.stream().map(ShipmentLocationTO::shipmentLocationTypeCode).toList();
    var polIndex = firstOccurrences.get(ShipmentLocationTypeCode.POL);
    var podIndex = firstOccurrences.get(ShipmentLocationTypeCode.POD);
    boolean insideVesselSegment = false;

    // Validate transport stages
    for (int i = 0; i < transportToList.size(); i++) {
      TransportTO currentLeg = transportToList.get(i);
      if (polIndex != null && i < polIndex && !transportToList.get(i).transportPlanStage().equals(TransportPlanStageCode.PRC)) {
        throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. Legs before POL must be PRC.");
      } else if (polIndex != null &&  podIndex != null && i > polIndex && i < podIndex && ! transportToList.get(i).transportPlanStage().equals(TransportPlanStageCode.MNC)) {
        throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. Legs between POL and POD must be MNC.");
      } else if (podIndex != null && i > podIndex && ! transportToList.get(i).transportPlanStage().equals(TransportPlanStageCode.ONC)) {
        throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. Legs after POD must be ONC.");
      }
    }
    // Validate PRE and PDE stages
    if (SlCodes.contains(ShipmentLocationTypeCode.PRE)
            && transportToList.get(0).transportPlanStage() != TransportPlanStageCode.PRC) {
      throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. First leg TransportPlanStageCode must be PRC if PRE is defined.");
    }
    if (SlCodes.contains(ShipmentLocationTypeCode.PDE)
            && transportToList.get(transportToList.size() - 1).transportPlanStage() != TransportPlanStageCode.ONC) {
      throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. Last legs TransportPlanStageCode must be ONC if PDE is defined.");
    }

  }

  void validateModeOfTransport(List<TransportTO> transportToList, Map<ShipmentLocationTypeCode, Integer> firstOccurrences) {
    Integer preIndex = firstOccurrences.get(ShipmentLocationTypeCode.PRE);
    Integer polIndex = firstOccurrences.get(ShipmentLocationTypeCode.POL);
    Integer podIndex = firstOccurrences.get(ShipmentLocationTypeCode.POD);
    Integer pdeIndex = firstOccurrences.get(ShipmentLocationTypeCode.PDE);

    for (int i = 0; i < transportToList.size(); i++) {
      TransportTO currentLeg = transportToList.get(i);

      // Check mode of transport
      if (preIndex != null && i == 0 && currentLeg.modeOfTransport() != null && currentLeg.modeOfTransport() == DCSATransportType.VESSEL) {
        throw ConcreteRequestErrorMessageException.invalidInput("First modeOfTransport must be non-VESSEL if PRE is defined.");
      }
      if (polIndex != null && preIndex != null && i < polIndex && currentLeg.modeOfTransport() != null && currentLeg.modeOfTransport() == DCSATransportType.VESSEL) {
        throw ConcreteRequestErrorMessageException.invalidInput("modeOfTransports before POL must be non-VESSEL.");
      }
      if (podIndex != null && pdeIndex != null  && i > pdeIndex && currentLeg.modeOfTransport() != null && currentLeg.modeOfTransport() == DCSATransportType.VESSEL) {
        throw ConcreteRequestErrorMessageException.invalidInput("modeOfTransports after PDE must be non-VESSEL.");
      }
      if (pdeIndex != null && i == transportToList.size()-1 && currentLeg.modeOfTransport() != null && currentLeg.modeOfTransport() == DCSATransportType.VESSEL) {
        throw ConcreteRequestErrorMessageException.invalidInput("Last modeOfTransport must be non-VESSEL if PDE is defined.");
      }
      if (polIndex != null && podIndex != null &&  polIndex < i && i <  podIndex
              && i == transportToList.size()-1 && currentLeg.modeOfTransport() != null && currentLeg.modeOfTransport() != DCSATransportType.VESSEL) {
        throw ConcreteRequestErrorMessageException.invalidInput("There must be at least one VESSEL leg in the transport plan, it must be between POL and POD if these are given.");
      }
    }
  }

  private Booking getBooking(ManageShipmentRequestTO shipmentRequestTO) {
    return bookingRepository.findBookingByCarrierBookingRequestReference(shipmentRequestTO.carrierBookingRequestReference())
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No booking with reference " + shipmentRequestTO.carrierBookingRequestReference()));
  }

  private EquipmentAssignment mapEquipmentAssignment(EquipmentAssignmentTO equipmentAssignmentTO) {
    List<Equipment> equipments = equipmentRepository.findByEquipmentReferenceIn(equipmentAssignmentTO.equipmentReferences());
    if (equipments.size() != equipmentAssignmentTO.equipmentReferences().size()) {
      Predicate<String> validReferences = (equipments.stream()
        .map(Equipment::getEquipmentReference)
        .collect(Collectors.toUnmodifiableSet())::contains);
      String badRef = equipmentAssignmentTO.equipmentReferences().stream()
        .filter(validReferences.negate())
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.internalServerError("Could not determine which equipment reference was bad!"));
      throw ConcreteRequestErrorMessageException.invalidInput("Unknown equipment reference: " + badRef);
    }
    return equipmentAssignmentMapper.toDAO(
      equipmentAssignmentTO,
      equipments
    );
  }
}
