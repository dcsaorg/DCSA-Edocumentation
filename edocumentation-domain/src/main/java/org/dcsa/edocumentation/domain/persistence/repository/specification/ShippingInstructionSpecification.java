package org.dcsa.edocumentation.domain.persistence.repository.specification;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction_;
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
      Join<ShippingInstruction, Shipment> ShippingInstructionShipmentJoin =
          root.join(ShippingInstruction_.SHIPMENTS, JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (null != filters.documentStatus) {
        Predicate predicate = builder.equal(root.get("documentStatus"), filters.documentStatus);
        predicates.add(predicate);
      }
      if (null != filters.carrierBookingReference && !filters.carrierBookingReference.isEmpty()) {
        Predicate predicate =
            builder
                .in(ShippingInstructionShipmentJoin.get("carrierBookingReference"))
                .value(filters.carrierBookingReference);
        predicates.add(predicate);
      }
      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }
}
