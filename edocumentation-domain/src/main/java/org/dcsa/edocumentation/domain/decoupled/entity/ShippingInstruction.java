package org.dcsa.edocumentation.domain.decoupled.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipping_instruction")
public class ShippingInstruction {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "shipping_instruction_reference", length = 100, nullable = false)
  private String shippingInstructionReference;

  @Column(name = "document_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private EblDocumentStatus documentStatus;

  @Column(name = "transport_document_type_code")
  @Enumerated(EnumType.STRING)
  private TransportDocumentTypeCode transportDocumentTypeCode;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "content", columnDefinition = "jsonb", nullable = false)
  private String content;

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  @Column(name = "created_date_time", nullable = false)
  private OffsetDateTime createdDateTime;

  @Column(name = "updated_date_time", nullable = false)
  protected OffsetDateTime updatedDateTime;

  public void lockVersion(OffsetDateTime lockTime) {
    this.validUntil = lockTime;
  }
}
