package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShipmentSummaryService;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.*;
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
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ShipmentSummaryController {
  private final ShipmentSummaryService shipmentSummaryService;

  @GetMapping(path = "${spring.application.bkg-context-path}" + "/shipment-summaries")
  @ResponseStatus(HttpStatus.OK)
  public List<ShipmentSummaryTO> getBookingSummaries(
      @RequestParam(required = false) BkgDocumentStatus documentStatus,
      @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false)
      @Min(0)
        int page,
      @RequestParam(
        value = Pagination.DCSA_PAGESIZE_PARAM_NAME,
        defaultValue = "100",
        required = false)
      @Min(1)
        int pageSize,
      @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false) String sort,
      @RequestParam(value = "API-Version", required = false) String apiVersion,
      HttpServletRequest request,
      HttpServletResponse response) {

    return Pagination.with(request, response, page, pageSize)
      .sortBy(sort, List.of(new Sort.Order(Sort.Direction.ASC, "shipmentCreatedDateTime")),
        Sorter.SortableFields.of(configureSortableFields()))
      .paginate(pageRequest -> shipmentSummaryService.findShipmentSummaries(pageRequest, documentStatus));

  }

  private String[] configureSortableFields() {
    return new String[]{
        "carrierBookingReference",
        "booking.carrierBookingRequestReference",
        "booking.documentStatus",
        "shipmentCreatedDateTime",
        "shipmentUpdatedDateTime",
        "termsAndConditions"};
  }
}
