package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.ConsignmentItemValidation;
import org.springframework.web.bind.annotation.Mapping;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "consignment_item")
@ConsignmentItemValidation(groups = AsyncShipperProvidedDataValidation.class)
public class ConsignmentItem {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "description_of_goods", nullable = false)
  private String descriptionOfGoods;

  @Column(name = "carrier_booking_reference", nullable = false, length = 35)
  private String carrierBookingReference;

  @Column(name = "commodity_subreference", nullable = false, length = 100)
  private String commoditySubreference;

  @ElementCollection
  @Column(name = "hs_code", nullable = false)
  @CollectionTable(name = "hs_code_item", joinColumns = @JoinColumn(name = "consignment_item_id"))
  @OrderColumn(name = "element_order")
  private List<String> hsCodes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipping_instruction_id", nullable = false)
  @Setter(AccessLevel.PACKAGE)
  private ShippingInstruction shippingInstruction;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipment_id", nullable = false)
  private Shipment shipment;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "consignmentItem")
  private Set<Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "consignment_item_id", referencedColumnName = "id", nullable = false)
  @OrderColumn(name = "list_order")
  // Since the cargoItem.id is generated it can happen that two cargoItems have the same values and
  // therefore cannot be added to the set
  private List<CargoItem> cargoItems;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "consignment_item_id")
  private List<CustomsReference> customsReferences;

  public void resolvedShipment(@NotNull Shipment shipment) {
    if (!this.getCarrierBookingReference().equals(shipment.getCarrierBookingReference())) {
      throw new IllegalArgumentException("Shipment had the wrong carrier booking reference");
    }
    this.shipment = shipment;
  }
}
