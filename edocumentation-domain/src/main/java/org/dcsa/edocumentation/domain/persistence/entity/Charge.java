package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PaymentTerm;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "charge")
public class Charge {

  @Id private String id;

  @Column(name = "transport_document_id")
  private UUID transportDocumentID;

  @Column(name = "booking_data_id")
  private UUID bookingDataID;

  @Column(name = "charge_name")
  private String chargeName;

  @Column(name = "currency_amount")
  private Double currencyAmount;

  @PseudoEnum(value = "currencycodes.csv")
  @Column(name = "currency_code")
  private String currencyCode;

  @Column(name = "payment_term_code")
  @Enumerated(EnumType.STRING)
  private PaymentTerm paymentTermCode;

  @Column(name = "calculation_basis")
  private String calculationBasis;

  @Column(name = "unit_price")
  private Double unitPrice;

  @Column(name = "quantity")
  private Double quantity;
}
