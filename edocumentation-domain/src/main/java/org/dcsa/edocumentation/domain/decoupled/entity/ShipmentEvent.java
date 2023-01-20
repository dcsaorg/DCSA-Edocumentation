package org.dcsa.edocumentation.domain.decoupled.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ShipmentEventTypeCode;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shipment_event")
public class ShipmentEvent extends Event {
  @Enumerated(EnumType.STRING)
  @Column(name = "shipment_event_type_code", nullable = false)
  private ShipmentEventTypeCode shipmentEventTypeCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_type_code", nullable = false)
  private DocumentTypeCode documentTypeCode;

  @Column(name = "document_id", nullable = false)
  private UUID documentID;

  @Column(name = "document_reference", nullable = false)
  private String documentReference;

  @Column(name = "reason", length = 100)
  private String reason;
}
