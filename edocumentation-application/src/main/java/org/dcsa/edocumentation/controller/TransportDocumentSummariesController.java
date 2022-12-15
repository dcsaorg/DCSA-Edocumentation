package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem_;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment_;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction_;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument_;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.service.TransportDocumentSummaryService;
import org.dcsa.edocumentation.transferobjects.TransportDocumentSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(
    value = "${spring.application.ebl-context-path}" + "/transport-document-summaries",
    produces = {MediaType.APPLICATION_JSON_VALUE})
public class TransportDocumentSummariesController {

  private final TransportDocumentSummaryService transportDocumentSummaryService;
  private final List<Sort.Order> defaultSort =
      List.of(
          new Sort.Order(
              Sort.Direction.ASC, TransportDocument_.TRANSPORT_DOCUMENT_CREATED_DATE_TIME));

  private final Sorter.SortableFields sortableFields =
      Sorter.SortableFields.of(
              Shipment_.CARRIER_BOOKING_REFERENCE, ShippingInstruction_.DOCUMENT_STATUS)
          .addMapping(
              Shipment_.CARRIER_BOOKING_REFERENCE,
              TransportDocument_.SHIPPING_INSTRUCTION
                  + "."
                  + ShippingInstruction_.CONSIGNMENT_ITEMS
                  + "."
                  + ConsignmentItem_.SHIPMENT
                  + "."
                  + Shipment_.CARRIER_BOOKING_REFERENCE)
          .addMapping(
              ShippingInstruction_.DOCUMENT_STATUS,
              TransportDocument_.SHIPPING_INSTRUCTION + "." + ShippingInstruction_.DOCUMENT_STATUS);

  @GetMapping
  public List<TransportDocumentSummaryTO> getTransportDocumentSummaries(
      @RequestParam(value = "carrierBookingReference", required = false)
          String carrierBookingReference,
      @RequestParam(value = "documentStatus", required = false) EblDocumentStatus documentStatus,
      @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false)
          @Min(0)
          int page,
      @RequestParam(
              value = Pagination.DCSA_PAGESIZE_PARAM_NAME,
              defaultValue = "100",
              required = false)
          @Min(1)
          int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "API-Version", required = false) String apiVersion,
      HttpServletRequest request,
      HttpServletResponse response) {

    return Pagination.with(request, response, page, pageSize)
        .sortBy(sort, defaultSort, sortableFields)
        .paginate(
            pageRequest ->
                transportDocumentSummaryService.getTransportDocumentSummaries(
                    TransportDocumentSummaryService.Filters.builder()
                        .carrierBookingReference(carrierBookingReference)
                        .documentStatus(documentStatus)
                        .pageRequest(pageRequest)
                        .build()));
  }
}
