package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShipmentSummaryService;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
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
public class ShipmentSummaryController {
  private final ShipmentSummaryService shipmentSummaryService;
  private final Paginator paginator;

  @GetMapping(path = "${spring.application.bkg-context-path}" + "/shipment-summaries")
  @ResponseStatus(HttpStatus.OK)
  public List<ShipmentSummaryTO> getBookingSummaries(
      @RequestParam(required = false) BkgDocumentStatus documentStatus,
      @RequestParam(required = false, defaultValue = "100") Integer limit,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "API-Version", required = false) String apiVersion,
      HttpServletRequest request,
      HttpServletResponse response) {

    Sorter sorter = configureSorter();
    Cursor cursor =
        paginator.parseRequest(request, new CursorDefaults(limit, sorter.parseSort(sort)));

    PagedResult<ShipmentSummaryTO> shipmentSummariesResults =
        shipmentSummaryService.findShipmentSummaries(cursor, documentStatus);

    paginator.setPageHeaders(request, response, cursor, shipmentSummariesResults);

    return shipmentSummariesResults.content();
  }

  // TODO optimization suggested in BookingSummaryController
  private Sorter configureSorter() {
    return new Sorter(
        List.of(new Cursor.SortBy(Sort.Direction.ASC, "carrierBookingReference")),
        "carrierBookingReference",
        "booking.carrierBookingRequestReference",
        "booking.documentStatus",
        "shipmentCreatedDateTime",
        "shipmentUpdatedDateTime",
        "termsAndConditions");
  }
}
