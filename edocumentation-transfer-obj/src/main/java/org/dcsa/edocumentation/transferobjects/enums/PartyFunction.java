package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PartyFunction {
  OS("Original shipper"),
  CN("Consignee"),
  COW("Invoice payer on behalf of the consignor(\"shipper\")"),
  COX("Invoice payer on behalf of the consignee"),
  MS("Document/message issuer/sender"),
  N1("First Notify Party"),
  N2("Second Notify Party"),
  NI("Other Notify Party"),
  DDR("Consignor's freight forwarder"),
  DDS("Consignee's freight forwarder"),
  HE("Carrier booking office(\"transportation office\")"),
  SCO("Service contract owner -Defined by DCSA"),
  BA("Booking Agency"),
  EBL("EBL Solution Provider")
  ;

  @Getter
  private final String value;
}
