package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "cargo_line_item")
public class CargoLineItem {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "shipping_marks", nullable = false)
  private String shippingMarks;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "cargo_item_id")
  private CargoItem cargoItem;
}
