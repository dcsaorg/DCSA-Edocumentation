package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.dcsa.skernel.infrastructure.validation.AtLeast;
import org.dcsa.skernel.infrastructure.validation.OneOf;

@AtLeast(nonNullsRequired = 1, fields = {"phone", "email"})
public record PartyContactDetailsTO(
  @NotBlank @Size(max = 100)
  String name,

  @Size(max = 30)
  String phone,

  @Size(max = 100) @Email
  String email
) {
  @Builder(toBuilder = true)
  public PartyContactDetailsTO { }
}
