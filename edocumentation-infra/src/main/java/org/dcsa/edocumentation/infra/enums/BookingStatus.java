package org.dcsa.edocumentation.infra.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingStatus extends StringEnum {
  public static final String RECEIVED = "RECEIVED";
  public static final String PENDING_UPDATE = "PENDING UPDATE";
  public static final String PENDING_UPDATES_CONFIRMATION = "PENDING UPDATES CONFIRMATION";
  public static final String PENDING_AMENDMENTS_APPROVAL = "PENDING AMENDMENTS APPROVAL";
  public static final String CONFIRMED = "CONFIRMED";
  public static final String REJECTED = "REJECTED";
  public static final String DECLINED = "DECLINED";
  public static final String CANCELLED = "CANCELLED";
  public static final String COMPLETED = "COMPLETED";
}
