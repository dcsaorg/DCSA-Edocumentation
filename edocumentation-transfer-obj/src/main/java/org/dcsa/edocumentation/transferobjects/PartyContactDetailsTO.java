package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PartyContactDetailsTO(
  @NotBlank @Size(max = 100)
  String name,

  @Size(max = 30)
  String phone,

  @Size(max = 100) @Email
  String email,

  @Size(max = 100)
  String url
) {
  @Builder(toBuilder = true)
  public PartyContactDetailsTO { }
}
