package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.infra.validation.StringEnumValidation;
import org.dcsa.edocumentation.service.BookingSummaryService;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class BookingSummaryController {

  private final BookingSummaryService service;

  @GetMapping(path = "${spring.application.bkg-context-path}" + "/booking-summaries")
  @ResponseStatus(HttpStatus.OK)
  public List<BookingSummaryTO> getBookingSummaries(
      @RequestParam(required = false)
      @StringEnumValidation(value = BookingStatus.class)
      String bookingStatus,
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
            List.of(new Sort.Order(Sort.Direction.ASC, "bookingRequestCreatedDateTime")),
            Sorter.SortableFields.of(configureSortableFields()))
        .paginate(pageRequest -> service.findBookingSummaries(pageRequest, bookingStatus));
  }

  // ToDo a shared-kernel feature where you can create a sorter based on a TO could be a nice
  // optimization
  private String[] configureSortableFields() {
    return new String[] {
      "bookingStatus",
      "bookingRequestCreatedDateTime",
      "bookingRequestUpdatedDateTime",
      "receiptTypeAtOrigin",
      "deliveryTypeAtDestination",
      "cargoMovementTypeAtOrigin",
      "cargoMovementTypeAtDestination",
      "serviceContractReference",
      "carrierExportVoyageNumber",
      "universalExportVoyageReference",
      "declaredValue",
      "declaredValueCurrency",
      "paymentTermCode",
      "isPartialLoadAllowed",
      "isExportDeclarationRequired",
      "exportDeclarationReference",
      "isImportLicenseRequired",
      "importLicenseReference",
      "isAMSACIFilingRequired",
      "isDestinationFilingRequired",
      "contractQuotationReference",
      "expectedDepartureDate",
      "expectedArrivalAtPlaceOfDeliveryStartDate",
      "expectedArrivalAtPlaceOfDeliveryEndDate",
      "transportDocumentTypeCode",
      "transportDocumentReference",
      "bookingChannelReference",
      "incoTerms",
      "communicationChannelCode",
      "isEquipmentSubstitutionAllowed"
    };
  }
}
