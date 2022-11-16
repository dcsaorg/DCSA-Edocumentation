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

  @Column(name = "cargo_line_item_id", nullable = false)
  private String cargoLineItemID;

}
