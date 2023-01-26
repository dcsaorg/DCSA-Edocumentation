package org.dcsa.edocumentation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking_;
import org.dcsa.edocumentation.service.BookingSummaryService;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter.SortableFields;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequestMapping("${spring.application.bkg-context-path}")
@RestController
@RequiredArgsConstructor
public class BookingSummaryController {

  private final SortableFields sortableFields =
    SortableFields.of(Booking_.BOOKING_REQUEST_CREATED_DATE_TIME, Booking_.BOOKING_REQUEST_UPDATED_DATE_TIME);
  private final List<Sort.Order> defaultSort =
    List.of(new Sort.Order(Direction.ASC, Booking_.BOOKING_REQUEST_CREATED_DATE_TIME));

  private final BookingSummaryService service;

  @GetMapping(path = "/booking-summaries")
  @ResponseStatus(HttpStatus.OK)
  public List<BookingSummaryTO> getBookingSummaries(
      @RequestParam(required = false) BkgDocumentStatus documentStatus,
      @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0) int page,
      @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1) int pageSize,
      @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false) String sort,
      @RequestParam(value = "API-Version", required = false) String apiVersion,
      HttpServletRequest request, HttpServletResponse response) {

    return Pagination.with(request, response, page, pageSize)
        .sortBy(sort, defaultSort, sortableFields)
        .paginate(pageRequest -> service.findBookingSummaries(pageRequest, documentStatus));
  }
}
