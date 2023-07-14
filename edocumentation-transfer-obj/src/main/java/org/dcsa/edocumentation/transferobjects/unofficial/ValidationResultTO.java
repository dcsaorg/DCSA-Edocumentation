package org.dcsa.edocumentation.transferobjects.unofficial;

public record ValidationResultTO<E extends Enum<E>>(
  E documentStatus,
  String reason
) {
}
