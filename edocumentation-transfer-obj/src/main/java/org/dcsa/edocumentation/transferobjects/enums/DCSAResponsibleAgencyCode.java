package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DCSAResponsibleAgencyCode {
  ISO("International Standards Organization"),
  UNECE("United Nations Economic Commission for Europe"),
  LLOYD("Lloyd's register of shipping"),
  BIC("Bureau International des Containeurs"),
  IMO("International Maritime Organization"),
  SCAC("Standard Carrier Alpha Code"),
  ITIGG("International Transport Implementation Guidelines Group"),
  ITU("International Telecommunication Union"),
  SMDG("Shipplanning Message Development Group"),
  EXIS("Exis Technologies Ltd."),
  FMC("Federal Maritime Commission"),
  CBSA("Canada Border Services Agency"),
  DCSA("Digital Container Shipping Association"),
  DID("Decentralized Identifier"),
  LEI("Legal Entity Identifier"),
  ZZZ("Mutually defined"),
  ;

  @Getter
  private final String value;
}
