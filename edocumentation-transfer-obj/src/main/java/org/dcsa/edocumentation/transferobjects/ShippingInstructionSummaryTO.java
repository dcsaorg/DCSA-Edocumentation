package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record ShippingInstructionSummaryTO(
  @NotBlank(message = "The attribute shippingInstructionReference is required.")
  @Size(max = 100, message = "The attribute shippingInstructionReference has a max size of 100.")
  String shippingInstructionReference,

  @NotNull(message = "The attribute documentStatus is required.")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  EblDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NotNull(message = "The attribute shippingInstructionCreatedDateTime is required.")
  OffsetDateTime shippingInstructionCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NotNull(message = "The attribute shippingInstructionUpdatedDateTime is required.")
  OffsetDateTime shippingInstructionUpdatedDateTime,

  @Size(max = 20, message = "The attribute amendToTransportDocument has a max size of 20.")
  String amendToTransportDocument,

  TransportDocumentTypeCode transportDocumentTypeCode,

  @NotNull(message = "The boolean attribute isShippedOnBoardType is required.")
  boolean isShippedOnBoardType,

  Integer numberOfCopiesWithCharges,

  Integer numberOfCopiesWithoutCharges,

  @JsonAlias({"numberOfOriginals", "requestedNumberOfOriginals"})
  Integer numberOfOriginalsWithCharges,

  Integer numberOfOriginalsWithOutCharges,

  @NotNull(message = "The boolean attribute isElectronic is required.")
  boolean isElectronic,

  @NotNull(message = "The boolean attribute isToOrder is required.")
  boolean isToOrder,

  boolean areChargesDisplayedOnOriginals,

  @Size(max = 250, message = "The attribute displayedNameForPlaceOfReceipt has a max size of 250.")
  String displayedNameForPlaceOfReceipt,

  @Size(max = 250, message = "The attribute displayedNameForPortOfLoad has a max size of 250.")
  String displayedNameForPortOfLoad,

  @Size(max = 250, message = "The attribute displayedNameForPortOfDischarge has a max size of 250.")
  String displayedNameForPortOfDischarge,

  @Size(max = 250, message = "The attribute displayedNameForPlaceOfDelivery has a max size of 250.")
  String displayedNameForPlaceOfDelivery,

  List<@Size(max = 100, message = "The attribute carrierBookingReference has a max size of 100.") String> carrierBookingReferences
) {
  @Builder
  public ShippingInstructionSummaryTO { }
}
