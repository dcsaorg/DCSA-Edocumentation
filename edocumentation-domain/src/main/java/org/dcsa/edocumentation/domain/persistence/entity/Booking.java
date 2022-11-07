package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@NamedEntityGraph(
  name = "graph.booking-summary",
  attributeNodes = {
    @NamedAttributeNode("vessel"),
    @NamedAttributeNode("modeOfTransport")
  }
)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
public class Booking {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "carrier_booking_request_reference", length = 100)
  private String carrierBookingRequestReference;

  @Column(name = "document_status")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private BkgDocumentStatus documentStatus = BkgDocumentStatus.RECE;

  @Column(name = "receipt_type_at_origin")
  @Enumerated(EnumType.STRING)
  private ReceiptDeliveryType receiptTypeAtOrigin;

  @Column(name = "delivery_type_at_destination")
  @Enumerated(EnumType.STRING)
  private ReceiptDeliveryType deliveryTypeAtDestination;

  @Column(name = "cargo_movement_type_at_origin")
  @Enumerated(EnumType.STRING)
  private CargoMovementType cargoMovementTypeAtOrigin;

  @Column(name = "cargo_movement_type_at_destination")
  @Enumerated(EnumType.STRING)
  private CargoMovementType cargoMovementTypeAtDestination;

  @Column(name = "booking_request_datetime")
  private OffsetDateTime bookingRequestCreatedDateTime;

  @Column(name = "service_contract_reference", length = 30)
  private String serviceContractReference;

  @Column(name = "payment_term_code")
  @Enumerated(EnumType.STRING)
  private PaymentTerm paymentTermCode;

  @Column(name = "is_partial_load_allowed")
  private Boolean isPartialLoadAllowed;

  @Column(name = "is_export_declaration_required")
  private Boolean isExportDeclarationRequired;

  @Column(name = "export_declaration_reference", length = 35)
  private String exportDeclarationReference;

  @Column(name = "is_import_license_required")
  private Boolean isImportLicenseRequired;

  @Column(name = "import_license_reference", length = 35)
  private String importLicenseReference;

  @Column(name = "is_ams_aci_filing_required")
  private Boolean isAMSACIFilingRequired;

  @Column(name = "is_destination_filing_required")
  private Boolean isDestinationFilingRequired;

  @Column(name = "contract_quotation_reference", length = 35)
  private String contractQuotationReference;

  @Column(name = "incoterms")
  @Enumerated(EnumType.STRING)
  private IncoTerms incoTerms;

  @Column(name = "expected_departure_date")
  private LocalDate expectedDepartureDate;

  @Column(name = "expected_arrival_at_place_of_delivery_start_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryStartDate;

  @Column(name = "expected_arrival_at_place_of_delivery_end_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryEndDate;

  @Column(name = "transport_document_type_code")
  @Enumerated(EnumType.STRING)
  private TransportDocumentTypeCode transportDocumentTypeCode;

  @Column(name = "transport_document_reference", length = 20)
  private String transportDocumentReference;

  @Column(name = "booking_channel_reference", length = 20)
  private String bookingChannelReference;

  @Column(name = "communication_channel_code")
  @Enumerated(EnumType.STRING)
  private CommunicationChannelCode communicationChannelCode;

  @Column(name = "is_equipment_substitution_allowed")
  private Boolean isEquipmentSubstitutionAllowed;

  @Column(name = "declared_value_currency_code", length = 3)
  private String declaredValueCurrency;

  @Column(name = "declared_value")
  private Float declaredValue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "pre_carriage_mode_of_transport_code")
  private ModeOfTransport modeOfTransport;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "vessel_id")
  private Vessel vessel;

  // ToDo only the required associations for booking summaries have been implemented

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  // updatedDateTime is metadata to avoid having to query shipment_event for updated dateTime.
  // This is not part of the official IM model. They are added in the sql only.
  @Column(name = "updated_date_time")
  protected OffsetDateTime bookingRequestUpdatedDateTime;
}
