package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.*;
import org.dcsa.edocumentation.service.ShipmentLocationService;
import org.dcsa.edocumentation.service.ShipmentTransportService;
import org.dcsa.edocumentation.service.mapping.ConfirmedEquipmentMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentCutOffTimeMapper;
import org.dcsa.edocumentation.service.mapping.AdvanceManifestFilingMapper;
import org.dcsa.edocumentation.service.mapping.AdvanceManifestFilingMapperImpl;
import org.dcsa.edocumentation.service.mapping.ShipmentMapper;
import org.dcsa.edocumentation.transferobjects.*;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;
import org.dcsa.edocumentation.transferobjects.unofficial.ManageShipmentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManageShipmentService {
  private final ShipmentRepository shipmentRepository;
  private final BookingRepository bookingRepository;
  private final ShipmentMapper shipmentMapper;
  private final CarrierRepository carrierRepository;
  private final BookingValidationService bookingValidationService;
  private final ShipmentLocationService shipmentLocationService;
  private final ShipmentTransportService shipmentTransportService;
  private final ConfirmedEquipmentMapper confirmedEquipmentMapper;
  private final ShipmentCutOffTimeMapper shipmentCutOffTimeMapper;

  private final AdvanceManifestFilingMapper advanceManifestFilingMapper;


  private final Random random = new SecureRandom();

  private static final Predicate<String> SOURCE_LOCATION_TYPE = Set.of(ShipmentLocationTypeCode.PRE.name(), ShipmentLocationTypeCode.POL.name())::contains;
  private static final Predicate<String> DESTINATION_LOCATION_TYPE = Set.of(ShipmentLocationTypeCode.POD.name(), ShipmentLocationTypeCode.PDE.name())::contains;
  public static final Predicate<ShipmentLocationTO> HAS_SOURCE_OR_DESTINATION_LOCATION =
          sl -> SOURCE_LOCATION_TYPE.test(sl.shipmentLocationTypeCode())
                  || DESTINATION_LOCATION_TYPE.test(sl.shipmentLocationTypeCode());

  public static final List<String> SHIPMENT_LOCATION_TYPE_CODE_ORDER =
          List.of(ShipmentLocationTypeCode.PRE.name(), ShipmentLocationTypeCode.POL.name(), ShipmentLocationTypeCode.POD.name(), ShipmentLocationTypeCode.PDE.name());


  private static final char[] VALID_CBR_AUTOGEN_CHARS = (
    // Exclude characters that look alike (I vs. 1, O vs 0)
    asciiRange('A', 'Z', 'I', 'O')
    + asciiRange('2', '9')

  ).toCharArray();

  private static String asciiRange(char start, char end, Character ... except) {
    StringBuilder range = new StringBuilder(end - start + 1 - except.length);  // Range is inclusive
    Set<Character> exceptions = Set.of(except);
    if (exceptions.contains(start) || exceptions.contains(end)) {
      throw new IllegalArgumentException("Range must not start / end on an exception");
    }
    var current = start;
    do {
      if (!exceptions.contains(current)) {
        range.append(current);
      }
    } while (++current <= end);
    return range.toString();
  }

  private String generateCarrierBookingReference() {
    char[] cbr = new char[35];
    for (int i = 0 ; i < cbr.length ; i++) {
      cbr[i] = VALID_CBR_AUTOGEN_CHARS[random.nextInt(VALID_CBR_AUTOGEN_CHARS.length)];
    }
    return new String(cbr);
  }

  private String generateCommoditySubreference() {
    char[] ref = new char[100];
    for (int i = 0 ; i < ref.length ; i++) {
      ref[i] = VALID_CBR_AUTOGEN_CHARS[random.nextInt(VALID_CBR_AUTOGEN_CHARS.length)];
    }
    return new String(ref);
  }

  private void assignCommoditySubreferences(
    Booking booking,
    List<@Valid List<@Valid @NotBlank @Size(max = 100) @Pattern(regexp = "^\\S(\\s+\\S+)*$") String>> subreferences
  ) {
    if (subreferences != null) {
      long totalCount = subreferences.stream().mapToLong(Collection::size).sum();
      long uniqueCount = subreferences.stream()
        .flatMap(Collection::stream)
        .distinct()
        .count();
      if (totalCount != uniqueCount) {
        throw ConcreteRequestErrorMessageException.invalidInput("All commoditySubreferences must be unique"
          + " across the shipment");
      }
      var requestedEquipments = booking.getRequestedEquipments();
      if (subreferences.size() != requestedEquipments.size()) {
        throw ConcreteRequestErrorMessageException.invalidInput("There are " + requestedEquipments.size()
         + " requested equipment groups but subreferences for " + subreferences.size()
         + " requested equipment groups. These two numbers should be the same.");
      }

      for (int i = 0 ; i < requestedEquipments.size() ; i++) {
        var commodities = requestedEquipments.get(i).getCommodities();
        var refs = subreferences.get(i);
        if (commodities.size() != refs.size()) {
          throw ConcreteRequestErrorMessageException.invalidInput("There are " + commodities.size()
            + " commodities in but subreferences for " + refs.size()
            + " commodities in the requested equipment group with index " + i + "."
            + " These two numbers should be the same.");
        }
        var j = 0;
        for (var commodity : commodities) {
          var ref = refs.get(j);
          if (commodity.getCommoditySubreference() != null && !commodity.getCommoditySubreference().equals(ref)) {
            throw ConcreteRequestErrorMessageException.invalidInput("The commodity at index [" + i + ", " + j
              + "] was already assigned the commodity subreference \"" + commodity.getCommoditySubreference()
              + "\". As an implementation detail, the RI does not support changing subreferences."
              + " When (re-)confirming the booking, you must reuse the same commodity subreference for commodities"
              + " that where already assigned a commodity subreference.");
          }
          commodity.assignSubreference(refs.get(j++));
        }
      }
    } else {
      booking.getRequestedEquipments().stream()
        .map(RequestedEquipmentGroup::getCommodities)
        .flatMap(Collection::stream)
        .filter(c -> c.getCommoditySubreference() == null)
        .forEach(commodity -> commodity.assignSubreference(generateCommoditySubreference()));
    }
  }

  @Transactional
  public ShipmentRefStatusTO create(ManageShipmentRequestTO shipmentRequestTO) {
    Booking booking = getBooking(shipmentRequestTO);
    var carrier = carrierRepository.findBySmdgCode(shipmentRequestTO.carrierSMDGCode());
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("Unrecognized SMDG LCL party code \""
        + shipmentRequestTO.carrierSMDGCode() + "\". Note the code may be valid but not loaded into this system.");
    }
    OffsetDateTime confirmationTime = OffsetDateTime.now();
    ValidationResult<BkgDocumentStatus> validationResult = bookingValidationService.validateBooking(booking, false);

    assignCommoditySubreferences(booking, shipmentRequestTO.commoditySubreferences());

    booking.confirm(confirmationTime);

    // FIXME: Check if the shipment (CBR) already exists and then attempt to reconfirm it if possible rather than
    //  die with a 409 Conflict (or create a duplicate or whatever happens). Probably just set "valid_until = now()"
    //  on the old shipment if it exists and then create a new one.
    //  On reconfirm: If a CBR is not provided, then keep the original CBR. Otherwise, if they are distinct, replace
    //  the old one with the new.
    Shipment shipment = Shipment.builder()
            // FIXME: The booking must be deeply cloned, so the shipment is not mutable via PUT /bookings/{...}
            .booking(booking)
            .carrier(carrier)
            .carrierBookingReference(Objects.requireNonNullElseGet(shipmentRequestTO.carrierBookingReference(), this::generateCarrierBookingReference))
            .shipmentCreatedDateTime(confirmationTime)
            .shipmentUpdatedDateTime(confirmationTime)
            .termsAndConditions(shipmentRequestTO.termsAndConditions())
            .build();

    shipment.assignAdvanceManifestFiling(
      Objects.requireNonNullElse(
          shipmentRequestTO.advanceManifestFilings(),
          Collections.<AdvanceManifestFilingTO>emptyList()
        ).stream()
        .map(advanceManifestFilingMapper::toDAO)
        .toList()
    );

    if (!validationResult.validationErrors().isEmpty()) {
      return shipmentMapper.toStatusDTO(shipment, validationResult.proposedStatus());
    }

    // FIXME: This should be processed into a status DTO rather than exception style.
    validateTransportPlans(shipmentRequestTO, shipmentRequestTO.shipmentLocations());

    shipment.assignConfirmedEquipments(
      Objects.requireNonNullElse(
        shipmentRequestTO.confirmedEquipments(),
        Collections.<ConfirmedEquipmentTO>emptyList()
      ).stream()
        .map(confirmedEquipmentMapper::toDAO)
        .toList()
    );

    shipment.assignShipmentCutOffTimes(
      Objects.requireNonNullElse(
          shipmentRequestTO.shipmentCutOffTimes(),
          Collections.<ShipmentCutOffTimeTO>emptyList()
        ).stream()
        .map(shipmentCutOffTimeMapper::toDAO)
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
    Map<String, Integer> firstOccurrences = new HashMap<>();
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
    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PRE.name()) && firstOccurrences.get(ShipmentLocationTypeCode.PRE.name()) != 0){
      throw ConcreteRequestErrorMessageException.invalidInput("PRE must be the first location if provided.");
    }

    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PDE.name()) && firstOccurrences.get(ShipmentLocationTypeCode.PDE.name()) != transportToList.size() - 1){
      throw ConcreteRequestErrorMessageException.invalidInput("PDE must be the last location if provided.");
    }
    // Check if PDE and POD, if present, are at the correct positions;
    if(firstOccurrences.containsKey(ShipmentLocationTypeCode.PDE.name())){
      if (firstOccurrences.get(ShipmentLocationTypeCode.PDE.name()) != transportToList.size() - 1) {
        throw  ConcreteRequestErrorMessageException.invalidInput("PDE must be the last location if provided");
      }
    } else if (firstOccurrences.containsKey(ShipmentLocationTypeCode.POD.name())) {
      if (firstOccurrences.get(ShipmentLocationTypeCode.POD.name()) != transportToList.size() - 1)
        throw  ConcreteRequestErrorMessageException.invalidInput("POD must be the last location if PDE is not provided.");
      }

      // Check that the first occurrences follow the expected order
      for (int i = 0; i < SHIPMENT_LOCATION_TYPE_CODE_ORDER.size() - 1; i++) {
        var current = SHIPMENT_LOCATION_TYPE_CODE_ORDER.get(i);
        var next = SHIPMENT_LOCATION_TYPE_CODE_ORDER.get(i + 1);

        if (firstOccurrences.containsKey(current) && firstOccurrences.containsKey(next) &&
                firstOccurrences.get(current) > firstOccurrences.get(next)) {
          throw ConcreteRequestErrorMessageException.invalidInput("Transport locations must follow the Shipment locations" +
                  " order: PRE, POL, POD, PDE.");
        }
    }

    // CHECK FOR: the minimum required length
    long countOfPreAndPde = shipmentLocationTOS.stream()
            .filter(sl -> sl.shipmentLocationTypeCode().equals(ShipmentLocationTypeCode.PRE.name())
                    || sl.shipmentLocationTypeCode().equals(ShipmentLocationTypeCode.PDE.name()))
            .count();

    if (countOfPreAndPde >= transportToList.size()) {
      throw ConcreteRequestErrorMessageException.invalidInput("Transport plan does not meet the minimum required length.");
    }
    validateTransportStages(transportToList, shipmentLocationTOS, firstOccurrences);
    validateModeOfTransport(transportToList, firstOccurrences);
     return true;
  }

  void validateTransportStages(List<TransportTO> transportToList, List<ShipmentLocationTO> shipmentLocationTOS, Map<String, Integer> firstOccurrences) {

    var SlCodes = shipmentLocationTOS.stream().map(ShipmentLocationTO::shipmentLocationTypeCode).toList();
    var polIndex = firstOccurrences.get(ShipmentLocationTypeCode.POL.name());
    var podIndex = firstOccurrences.get(ShipmentLocationTypeCode.POD.name());
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
    if (SlCodes.contains(ShipmentLocationTypeCode.PRE.name())
            && transportToList.get(0).transportPlanStage() != TransportPlanStageCode.PRC) {
      throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. First leg TransportPlanStageCode must be PRC if PRE is defined.");
    }
    if (SlCodes.contains(ShipmentLocationTypeCode.PDE.name())
            && transportToList.get(transportToList.size() - 1).transportPlanStage() != TransportPlanStageCode.ONC) {
      throw ConcreteRequestErrorMessageException.invalidInput("Invalid transport plan stage Code. Last legs TransportPlanStageCode must be ONC if PDE is defined.");
    }

  }

  void validateModeOfTransport(List<TransportTO> transportToList, Map<String, Integer> firstOccurrences) {
    Integer preIndex = firstOccurrences.get(ShipmentLocationTypeCode.PRE.name());
    Integer polIndex = firstOccurrences.get(ShipmentLocationTypeCode.POL.name());
    Integer podIndex = firstOccurrences.get(ShipmentLocationTypeCode.POD.name());
    Integer pdeIndex = firstOccurrences.get(ShipmentLocationTypeCode.PDE.name());

    for (int i = 0; i < transportToList.size(); i++) {
      TransportTO currentLeg = transportToList.get(i);

      // Check mode of transport
      if (preIndex != null && i == 0 && currentLeg.modeOfTransport() != null && Objects.equals(currentLeg.modeOfTransport(), DCSATransportType.VESSEL.name())) {
        throw ConcreteRequestErrorMessageException.invalidInput("First modeOfTransport must be non-VESSEL if PRE is defined.");
      }
      if (polIndex != null && preIndex != null && i < polIndex && currentLeg.modeOfTransport() != null && Objects.equals(currentLeg.modeOfTransport(), DCSATransportType.VESSEL.name())) {
        throw ConcreteRequestErrorMessageException.invalidInput("modeOfTransports before POL must be non-VESSEL.");
      }
      if (podIndex != null && pdeIndex != null  && i > pdeIndex && currentLeg.modeOfTransport() != null && Objects.equals(currentLeg.modeOfTransport(), DCSATransportType.VESSEL.name())) {
        throw ConcreteRequestErrorMessageException.invalidInput("modeOfTransports after PDE must be non-VESSEL.");
      }
      if (pdeIndex != null && i == transportToList.size()-1 && currentLeg.modeOfTransport() != null && Objects.equals(currentLeg.modeOfTransport(), DCSATransportType.VESSEL.name())) {
        throw ConcreteRequestErrorMessageException.invalidInput("Last modeOfTransport must be non-VESSEL if PDE is defined.");
      }
      if (polIndex != null && podIndex != null &&  polIndex < i && i <  podIndex
              && i == transportToList.size()-1 && currentLeg.modeOfTransport() != null && Objects.equals(currentLeg.modeOfTransport(), DCSATransportType.VESSEL.name())) {
        throw ConcreteRequestErrorMessageException.invalidInput("There must be at least one VESSEL leg in the transport plan, it must be between POL and POD if these are given.");
      }
    }
  }

  private Booking getBooking(ManageShipmentRequestTO shipmentRequestTO) {
    return bookingRepository.findBookingByCarrierBookingRequestReference(shipmentRequestTO.carrierBookingRequestReference())
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No booking with reference " + shipmentRequestTO.carrierBookingRequestReference()));
  }

}
