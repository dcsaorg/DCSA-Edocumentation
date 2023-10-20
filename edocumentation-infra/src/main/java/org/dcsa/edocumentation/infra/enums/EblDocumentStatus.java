package org.dcsa.edocumentation.infra.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EblDocumentStatus implements StringEnum {
  public static final String RECEIVED = "RECEIVED";
  public static final String PENDING_UPDATE = "PENDING UPDATE";
  public static final String DRAFT = "DRAFT";
  public static final String PENDING_APPROVAL = "PENDING APPROVAL";
  public static final String APPROVED = "APPROVED";
  public static final String ISSUED = "ISSUED";
  public static final String SURRENDERED = "SURRENDERED";
  public static final String VOID = "VOID";
  public static final String REJECTED = "REJECTED";
}
