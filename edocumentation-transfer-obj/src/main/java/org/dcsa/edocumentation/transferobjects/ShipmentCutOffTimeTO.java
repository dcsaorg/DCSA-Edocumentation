package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.PseudoEnum;

public record ShipmentCutOffTimeTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute cutOffDateTimeCode is required.")
        @Size(max = 3)
        @PseudoEnum("cutofftimecodes.csv")
        String cutOffDateTimeCode,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute cutOffDateTime is required.")
        OffsetDateTime cutOffDateTime) {

  @Builder(toBuilder = true)
  public ShipmentCutOffTimeTO {}
}
