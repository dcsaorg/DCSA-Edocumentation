package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.EquipmentAssignment;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentAssignmentMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentMapper;
import org.dcsa.edocumentation.transferobjects.unofficial.EquipmentAssignmentTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ManageShipmentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.repository.CarrierRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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


  @Transactional
  public ShipmentRefStatusTO create(ManageShipmentRequestTO shipmentRequestTO) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(shipmentRequestTO.carrierBookingRequestReference())
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No booking with reference " + shipmentRequestTO.carrierBookingRequestReference()));
    Carrier carrier = carrierRepository.findBySmdgCode(shipmentRequestTO.carrierSMDGCode());
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("Unrecognized SMDG LCL party code \""
        + shipmentRequestTO.carrierSMDGCode() + "\". Note the code may be valid but not loaded into this system.");
    }
    OffsetDateTime confirmationTime = OffsetDateTime.now();
    shipmentEventRepository.save(booking.confirm(confirmationTime));

    Shipment shipment = Shipment.builder()
      .booking(booking)
      .carrier(carrier)
      .carrierBookingReference(shipmentRequestTO.carrierBookingReference())
      .shipmentCreatedDateTime(confirmationTime)
      .shipmentUpdatedDateTime(confirmationTime)
      .termsAndConditions(shipmentRequestTO.termsAndConditions())
      .build();

    shipment.assignEquipments(
      Objects.requireNonNullElse(
        shipmentRequestTO.equipmentAssignments(),
        Collections.<EquipmentAssignmentTO>emptyList()
      ).stream()
        .map(this::mapEquipmentAssignment)
        .toList()
    );

    shipment = shipmentRepository.save(shipment);
    return shipmentMapper.toStatusDTO(shipment, booking.getDocumentStatus());
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
