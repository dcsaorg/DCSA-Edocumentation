package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestedChangeTO(
        @Size(max = 500) String path,
        @NotBlank
        @Size(max = 500) String message
) {

}
