package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CutOffDateTimeCode {
  DCO ("Documentation cut-off"),
  VCO ("VGM cut-off"),
  FCO ("FCL delivery cut-off"),
  LCO ("LCL delivery cut-off"),
  ECP ("Empty container pick-up date and time"),
  EFC ("Earliest full-container delivery date"),
  AFD ("AMS Filing Due date")
  ;
  @Getter
  private final String value;
  }
