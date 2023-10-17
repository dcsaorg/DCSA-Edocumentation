package org.dcsa.edocumentation.domain.persistence.repository.specification;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransportDocumentSpecification {

  @Builder(toBuilder = true)
  public static class Filters {
    private List<String> carrierBookingReference;
    private String documentStatus;
    private PageRequest pageRequest;
  }

  public static Specification<TransportDocument> withFilters(final Filters filters) {

    return (root, query, builder) -> {
      Join<TransportDocument, ShippingInstruction> tdSiJoin =
          root.join(TransportDocument_.SHIPPING_INSTRUCTION, JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();



      if (null != filters.documentStatus) {
        Predicate docStatusPredicate =
          builder.equal(
            tdSiJoin.get(ShippingInstruction_.DOCUMENT_STATUS), filters.documentStatus);
        predicates.add(docStatusPredicate);
      }

      if (null != filters.carrierBookingReference) {
        var subquery = query.subquery(UUID.class);
        var subqueryRoot = subquery.from(ShippingInstruction.class);
        var consignmentItemRoot = subqueryRoot.join(ShippingInstruction_.CONSIGNMENT_ITEMS);
        var consignmentItemShipmentJoin = consignmentItemRoot.join(ConsignmentItem_.SHIPMENT, JoinType.LEFT);
        subquery.select(subqueryRoot.get(ShippingInstruction_.ID));
        subquery.where(builder
          .in(consignmentItemShipmentJoin.get(Shipment_.CARRIER_BOOKING_REFERENCE))
          .value(filters.carrierBookingReference));
        Predicate predicate = builder.in(
          root.get(TransportDocument_.SHIPPING_INSTRUCTION).get(ShippingInstruction_.ID)
        ).value(subquery);
        predicates.add(predicate);
      }

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
