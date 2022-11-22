package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransportDocumentSpecification {

  @Builder(toBuilder = true)
  public static class Filters {
    private List<String> carrierBookingReference;
    private EblDocumentStatus documentStatus;
    private PageRequest pageRequest;
  }

  public static Specification<TransportDocument> withFilters(final Filters filters) {

    return (root, query, builder) -> {
      Join<TransportDocument, ShippingInstruction> tdSiJoin =
          root.join(TransportDocument_.SHIPPING_INSTRUCTION, JoinType.LEFT);
      Join<ShippingInstruction, ConsignmentItem> siCiJoin =
          tdSiJoin.join(ShippingInstruction_.CONSIGNMENT_ITEMS, JoinType.LEFT);
      Join<ConsignmentItem, Shipment> ciSiJoin =
          siCiJoin.join(ConsignmentItem_.SHIPMENT, JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();

      if (null != filters.carrierBookingReference) {
        Predicate carrierBkgRefPredicate =
            builder
                .in(ciSiJoin.get(Shipment_.CARRIER_BOOKING_REFERENCE))
                .value(filters.carrierBookingReference);
        predicates.add(carrierBkgRefPredicate);
        query.distinct(true);
      }

      if (null != filters.documentStatus) {
        Predicate docStatusPredicate =
            builder.equal(
                tdSiJoin.get(ShippingInstruction_.DOCUMENT_STATUS), filters.documentStatus);
        predicates.add(docStatusPredicate);
      }

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
