package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent_;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.edocumentation.domain.decoupled.repository.specification.ShipmentEventSpecification.ShipmentEventFilters;
import org.dcsa.edocumentation.service.unofficial.ShipmentEventService;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter.SortableFields;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ShipmentEventController {
  private final ShipmentEventService shipmentEventService;

  private final SortableFields sortableFields =
    SortableFields.of(ShipmentEvent_.EVENT_CREATED_DATE_TIME);
  private final List<Sort.Order> defaultSort =
    List.of(new Sort.Order(Direction.ASC, ShipmentEvent_.EVENT_CREATED_DATE_TIME));

  @GetMapping(path = "/unofficial${spring.application.bkg-context-path}/shipment-events")
  @ResponseStatus(HttpStatus.OK)
  public List<ShipmentEvent> getEvents(
    @RequestParam(value = "shipmentEventTypeCode", required = false)
    ShipmentEventTypeCode shipmentEventTypeCode,

    @RequestParam(value = "documentTypeCode", required = false)
    DocumentTypeCode documentTypeCode,

    @RequestParam(value = "documentID", required = false)
    UUID documentID,

    @RequestParam(value = "documentReference", required = false)
    String documentReference,

    @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0)
    int page,

    @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1)
    int pageSize,

    @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false)
    String sort,

    HttpServletRequest request, HttpServletResponse response
  ) {
    return Pagination.with(request, response, page, pageSize)
      .sortBy(sort, defaultSort, sortableFields)
      .paginate(pageRequest -> shipmentEventService.findAll(pageRequest, ShipmentEventFilters.builder()
        .shipmentEventTypeCode(shipmentEventTypeCode)
        .documentTypeCode(documentTypeCode)
        .documentID(documentID)
        .documentReference(documentReference)
        .build()));
  }
}
