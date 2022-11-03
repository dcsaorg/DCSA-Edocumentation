package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.service.ShippingInstructionSummaryService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
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
public class ShippingInstructionSummaryController {

  private final ShippingInstructionSummaryService service;

  @GetMapping(path = "${spring.application.ebl-context-path}" + "/shipping-instructions-summaries")
  @ResponseStatus(HttpStatus.OK)
  List<ShippingInstructionSummaryTO> getShippingInstructionSummaries(
      @RequestParam(required = false) EblDocumentStatus documentStatus,
      @RequestParam(required = false) String carrierBookingReference,
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
        .sortBy(
            sort,
            List.of(new Sort.Order(Sort.Direction.ASC, "shippingInstructionCreatedDateTime")),
            Sorter.SortableFields.of(configureSortableFields())
                .addMapping("shippingInstructionCreatedDateTime", "createdDateTime")
                .addMapping("shippingInstructionUpdatedDateTime", "updatedDateTime"))
        .paginate(
            pageRequest ->
                service.findShippingInstructionSummaries(pageRequest, documentStatus, carrierBookingReference));
  }
  private String[] configureSortableFields() {
    return new String[] {
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
      "displayedNameForPlaceOfDelivery"
    };
  }
}
