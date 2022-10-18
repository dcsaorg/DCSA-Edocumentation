package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommunicationChannelCode {
  EI("EDI transmission"),
  EM("Email"),
  AO("API");

  private final String value;
}
