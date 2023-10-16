package org.dcsa.edocumentation.domain.persistence.entity.unofficial;


import java.util.List;

public record ValidationResult<E> (
  E proposedStatus,
  List<String> validationErrors
) {

  public String presentErrors(int sizeLimit) {
    if (validationErrors.isEmpty()) {
      return "";
    }
    if (sizeLimit < 100) {
      throw new IllegalArgumentException("sizeLimit must be larger than 100");
    }
    StringBuilder b = new StringBuilder();
    String header = "Reasons:\n";
    final int headerLen = header.length();

    // If there is only one error, or we can only fit the first error, just report that back.
    // Otherwise, present a list of errors.

    if (validationErrors.size() == 1 || sizeLimit <= validationErrors.get(0).length() + headerLen + 4) {
      b.append(validationErrors.get(0));
      if (b.length() > sizeLimit) {
        // Theoretical possibility
        b.setLength(sizeLimit - 4);
        b.append(" ...");
      }
    } else {
      b.append(header);
      int remainingSize = sizeLimit - headerLen;
      for (String e : validationErrors) {
        if (e.length() + 4 >= remainingSize) {
          // Ideally we would mark it as truncated somehow, but seems to be more effort than what it is worth.
          assert b.length() > headerLen;
          break;
        }
        b.append(" * ").append(e).append("\n");
      }
    }
    return b.toString();
  }
}
