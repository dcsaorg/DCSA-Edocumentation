package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "consignment_item")
public class ConsignmentItem {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "description_of_goods", nullable = false)
  private String descriptionOfGoods;

  @ElementCollection
  @Column(name = "hs_code", nullable = false)
  @CollectionTable(name = "hs_code_item", joinColumns = @JoinColumn(name = "consignment_item_id"))
  @OrderColumn(name = "element_order")
  private List<String> hsCodes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipping_instruction_id", nullable = false)
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
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "consignment_item_id", referencedColumnName = "id", nullable = false)
  // Since the cargoItem.id is generated it can happen that two cargoItems have the same values and
  // therefore cannot be added to the set
  private List<CargoItem> cargoItems;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "consignment_item_id")
  private List<CustomsReference> customsReferences;
}
