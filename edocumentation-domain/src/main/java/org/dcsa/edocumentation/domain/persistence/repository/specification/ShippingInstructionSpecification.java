package org.dcsa.edocumentation.domain.persistence.repository.specification;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class ShippingInstructionSpecification {

  public record ShippingInstructionFilters(
      List<String> carrierBookingReference, EblDocumentStatus documentStatus) {
    @Builder
    public ShippingInstructionFilters {}
  }

  public static Specification<ShippingInstructionSpecification> withFilters(
      final ShippingInstructionFilters filters) {

    return (root, query, builder) -> {
      Join<ShippingInstruction, ConsignmentItem> shippingInstructionConsignmentItemJoin = root.join(ShippingInstruction_.CONSIGNMENT_ITEMS, JoinType.LEFT);
      Join<ConsignmentItem, Shipment> consignmentItemShipmentJoin = shippingInstructionConsignmentItemJoin.join(ConsignmentItem_.SHIPMENT, JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (null != filters.documentStatus) {
        Predicate predicate = builder.equal(root.get("documentStatus"), filters.documentStatus);
        predicates.add(predicate);
      }
      if (null != filters.carrierBookingReference && !filters.carrierBookingReference.isEmpty()) {
        Predicate predicate =
            builder
                .in(consignmentItemShipmentJoin.get("carrierBookingReference"))
                .value(filters.carrierBookingReference);
        predicates.add(predicate);
      }
      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
