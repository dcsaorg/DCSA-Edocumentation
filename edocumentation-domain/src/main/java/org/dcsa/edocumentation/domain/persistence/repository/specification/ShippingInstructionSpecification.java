package org.dcsa.edocumentation.domain.persistence.repository.specification;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@UtilityClass
public class ShippingInstructionSpecification {

  public record ShippingInstructionFilters(
      List<String> carrierBookingReference, String documentStatus) {
    @Builder
    public ShippingInstructionFilters {}
  }

  public static Specification<ShippingInstruction> withFilters(
      final ShippingInstructionFilters filters) {

    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (null != filters.documentStatus) {
        Predicate predicate = builder.equal(root.get(ShippingInstruction_.DOCUMENT_STATUS), filters.documentStatus);
        predicates.add(predicate);
      }
      if (null != filters.carrierBookingReference && !filters.carrierBookingReference.isEmpty()) {
        var subquery = query.subquery(UUID.class);
        var subqueryRoot = subquery.from(ShippingInstruction.class);
        var consignmentItemRoot = subqueryRoot.join(ShippingInstruction_.CONSIGNMENT_ITEMS);
        var consignmentItemShipmentJoin = consignmentItemRoot.join(ConsignmentItem_.SHIPMENT, JoinType.LEFT);
        subquery.select(subqueryRoot.get(ShippingInstruction_.ID));
        subquery.where(builder
          .in(consignmentItemShipmentJoin.get(Shipment_.CARRIER_BOOKING_REFERENCE))
          .value(filters.carrierBookingReference));
        Predicate predicate = builder.in(root.get(ShippingInstruction_.ID)).value(subquery);
        predicates.add(predicate);
      }
      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
