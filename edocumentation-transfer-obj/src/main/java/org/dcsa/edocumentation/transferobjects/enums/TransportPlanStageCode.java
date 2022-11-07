package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TransportPlanStageCode {
  PRC ("Pre-Carriage"),
  MNC ("Main Carriage Transport"),
  ONC ("On-Carriage Transport")
  ;
  @Getter
  private final String value;
  }
