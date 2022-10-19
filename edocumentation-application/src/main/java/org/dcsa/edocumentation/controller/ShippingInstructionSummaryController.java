package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.CursorDefaults;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.skernel.infrastructure.pagination.Paginator;
import org.dcsa.skernel.infrastructure.sorting.Sorter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ShippingInstructionSummaryController {
  private final ShippingInstructionService service;
  private final Paginator paginator;

  @GetMapping(path = "${spring.application.ebl-context-path}" + "/shipping-instructions-summaries")
  @ResponseStatus(HttpStatus.OK)
  List<ShippingInstructionSummaryTO> getShippingInstructionSummaries(
    @RequestParam(required = false) EblDocumentStatus documentStatus,
    @RequestParam(required = false) String carrierBookingReference,
    @RequestParam(required = false, defaultValue = "100") Integer limit,
    @RequestParam(value = "sort", required = false) String sort,
    @RequestParam(value = "API-Version", required = false) String apiVersion,
    HttpServletRequest request,
    HttpServletResponse response){

    Sorter sorter = configureSorter();
    Cursor cursor =
      paginator.parseRequest(request, new CursorDefaults(limit, sorter.parseSort(sort)));
    PagedResult<ShippingInstructionSummaryTO> shippingInstructionSummaries =
      service.findShippingInstructionSummaries(cursor, documentStatus, carrierBookingReference);
    paginator.setPageHeaders(request, response, cursor, shippingInstructionSummaries);

    return shippingInstructionSummaries.content();

  }
  // TODO optimization suggested in BookingSummaryController
  private Sorter configureSorter() {
    return new Sorter(
      List.of(new Cursor.SortBy(Sort.Direction.ASC, "shippingInstructionReference")),
      "shippingInstructionReference",
      "documentStatus",
      "shippingInstructionCreatedDateTime",
      "shippingInstructionUpdatedDateTime",
      "amendToTransportDocument",
      "transportDocumentTypeCode",
      "numberOfCopies",
      "numberOfOriginals",
      "displayedNameForPlaceOfReceipt",
      "displayedNameForPortOfLoad",
      "displayedNameForPortOfDischarge",
      "displayedNameForPlaceOfDelivery");
  }
}
