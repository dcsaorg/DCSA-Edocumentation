package org.dcsa.edocumentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.TransportDocumentService;
import org.dcsa.edocumentation.transferobjects.TransportDocumentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Size;

@RequiredArgsConstructor
@RestController
@RequestMapping(
  value = "${spring.application.ebl-context-path}" + "/transport-documents",
  produces = {MediaType.APPLICATION_JSON_VALUE})
public class TransportDocumentController {

  private final TransportDocumentService transportDocumentService;

  @GetMapping("/{transportDocumentReference}")
  @ResponseStatus(HttpStatus.OK)
  public TransportDocumentTO getTransportDocumentByReference(@PathVariable("transportDocumentReference") @Size(max = 20) String transportDocumentReference) {
    return transportDocumentService.findByReference(transportDocumentReference).orElseThrow(
      () ->
        ConcreteRequestErrorMessageException.notFound(
          "No transport document found with transportDocumentReference: "
            + transportDocumentReference));
  }

  @PatchMapping("/{transportDocumentReference}")
  @ResponseStatus(HttpStatus.OK)
  public TransportDocumentRefStatusTO approveTransportDocumentByReference(
    @PathVariable("transportDocumentReference") @Size(max = 20) String transportDocumentReference,
    @Valid @RequestBody ApproveTDPayload ignored
  ) {
    return transportDocumentService.approveTransportDocument(transportDocumentReference).orElseThrow(
      () ->
        ConcreteRequestErrorMessageException.notFound(
          "No transport document found with transportDocumentReference: "
            + transportDocumentReference));
  }

  private record ApproveTDPayload(
    @EnumSubset(anyOf = "APPROVED")
    String documentStatus
  ) {}
}
