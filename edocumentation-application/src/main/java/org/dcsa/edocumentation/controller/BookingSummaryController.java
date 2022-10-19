package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.BookingSummaryService;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
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
public class BookingSummaryController {

  private final BookingSummaryService service;
  private final Paginator paginator;

  @GetMapping(path = "${spring.application.bkg-context-path}" + "/booking-summaries")
  @ResponseStatus(HttpStatus.OK)
  public List<BookingSummaryTO> getBookingSummaries(
      @RequestParam(required = false) BkgDocumentStatus documentStatus,
      @RequestParam(required = false, defaultValue = "100") Integer limit,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "API-Version", required = false) String apiVersion,
      HttpServletRequest request,
      HttpServletResponse response) {

    Sorter sorter = configureSorter();
    Cursor cursor =
        paginator.parseRequest(request, new CursorDefaults(limit, sorter.parseSort(sort)));

    PagedResult<BookingSummaryTO> bookingSummaryResults =
        service.findBookingSummaries(cursor, documentStatus);
    paginator.setPageHeaders(request, response, cursor, bookingSummaryResults);

    return bookingSummaryResults.content();
  }

  // ToDo a shared-kernel feature where you can create a sorter based on a TO could be a nice
  // optimization
  private Sorter configureSorter() {
    return new Sorter(
        List.of(new Cursor.SortBy(Sort.Direction.ASC, "carrierBookingRequestReference")),
        "carrierBookingRequestReference",
        "documentStatus",
        "bookingRequestCreatedDateTime",
        "bookingRequestUpdatedDateTime",
        "receiptTypeAtOrigin",
        "deliveryTypeAtDestination",
        "cargoMovementTypeAtOrigin",
        "cargoMovementTypeAtDestination",
        "serviceContractReference",
        "vesselName",
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
        "isEquipmentSubstitutionAllowed",
        "vesselIMONumber",
        "preCarriageModeOfTransportCode");
  }
}
