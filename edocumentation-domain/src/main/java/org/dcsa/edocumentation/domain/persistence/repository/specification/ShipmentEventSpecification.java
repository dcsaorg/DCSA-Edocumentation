package org.dcsa.edocumentation.domain.persistence.repository.specification;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent_;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ShipmentEventTypeCode;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

@Slf4j
@UtilityClass
public class ShipmentEventSpecification {

  public record ShipmentEventFilters(
    ShipmentEventTypeCode shipmentEventTypeCode,
    DocumentTypeCode documentTypeCode,
    UUID documentID,
    String documentReference
  ) {
    @Builder
    public ShipmentEventFilters { }
  }

  public static Specification<ShipmentEvent> withFilters(final ShipmentEventFilters filters) {
    log.debug("Searching based on {}", filters);

    return (Root<ShipmentEvent> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      BiConsumer<String, Object> addEquals = (String field, Object filterValue) -> {
        if (filterValue != null) {
          predicates.add(builder.equal(root.get(field), filterValue));
        }
      };

      addEquals.accept(ShipmentEvent_.SHIPMENT_EVENT_TYPE_CODE, filters.shipmentEventTypeCode);
      addEquals.accept(ShipmentEvent_.DOCUMENT_TYPE_CODE, filters.documentTypeCode);
      addEquals.accept(ShipmentEvent_.DOCUMENT_ID, filters.documentID);
      addEquals.accept(ShipmentEvent_.DOCUMENT_REFERENCE, filters.documentReference);

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
