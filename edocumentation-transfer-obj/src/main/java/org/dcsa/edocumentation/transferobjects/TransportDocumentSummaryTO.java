package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record TransportDocumentSummaryTO (

  @Size(max = 20)
  String transportDocumentReference,

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String shippingInstructionReference,

  EblDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  OffsetDateTime transportDocumentCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  OffsetDateTime transportDocumentUpdatedDateTime,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate issueDate,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate shippedOnBoardDate,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate receivedForShipmentDate,

  Integer numberOfOriginalsWithCharges,

  Integer numberOfOriginalsWithoutCharges,

  @Size(max = 4)
  String carrierCode,

  CarrierCodeListProvider carrierCodeListProvider,

  PartyTO issuingParty,

  Integer numberOfRiderPages,

  List<String> carrierBookingReferences
){

  @Builder
  public TransportDocumentSummaryTO{}
}
