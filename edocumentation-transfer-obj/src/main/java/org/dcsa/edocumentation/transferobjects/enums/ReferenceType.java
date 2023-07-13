package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReferenceType {
  FF("Reference assigned to the shipment by the freight forwarder."),
  SI("Reference assigned to the shipment by the shipper."),
  SPO("Shippers Purchase Order Reference"),
  CPO("Consignees Purchase Order Reference"),
  AAO("Reference assigned to the shipment by the consignee."),
  ECR("Unique identifier to enable release of the container from a carrier nominated depot"),
  CSI("Unique Shipment ID for the booking in the Shipper or Forwarder system. Used to identify the booking along with the Booking party."),
  BPR("A unique identifier provided by a booking party in the booking request."),
  BID("The associated booking request ID provided by the shipper."),
  ;

  @Getter
  private String description;
}
