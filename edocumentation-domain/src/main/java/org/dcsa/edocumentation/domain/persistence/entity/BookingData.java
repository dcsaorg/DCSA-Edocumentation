package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.domain.validations.*;

@NamedEntityGraph(
  name = "graph.booking-data",
  attributeNodes = {
    @NamedAttributeNode("vessel"),
    @NamedAttributeNode("placeOfIssue"),
    @NamedAttributeNode("invoicePayableAt"),
    @NamedAttributeNode("references"),
    @NamedAttributeNode(value = "documentParties", subgraph = "graph.documentParties"),
    @NamedAttributeNode("shipmentLocations"),
    @NamedAttributeNode("shipmentCutOffTimes"),
    @NamedAttributeNode("carrierClauses"),
    @NamedAttributeNode("confirmedEquipments"),
    @NamedAttributeNode("charges"),
    @NamedAttributeNode("carrier"),
  },
  subgraphs = {
    @NamedSubgraph(
      name = "graph.documentParties",
      attributeNodes = {
        @NamedAttributeNode("displayedAddress"),
        @NamedAttributeNode(value = "party", subgraph = "graph.party")
      }),
    @NamedSubgraph(
      name = "graph.party",
      attributeNodes = {@NamedAttributeNode("partyContactDetails")}),
    @NamedSubgraph(
      name = "subgraph.shipmentLocations",
      attributeNodes = {@NamedAttributeNode("location")}),
    @NamedSubgraph(
      name = "subgraph.shipmentTransports",
      attributeNodes = {
        @NamedAttributeNode("loadLocation"),
        @NamedAttributeNode("dischargeLocation"),
      }),
  })
@Slf4j
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking_data")
@BookingDataValidation(groups = AsyncShipperProvidedDataValidation.class)
public class BookingData {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  private UUID id;

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
  @PseudoEnum(value = "incotermscodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String incoTerms;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "invoice_payable_at_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location invoicePayableAt;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
  @Column(name = "expected_departure_date")
  private LocalDate expectedDepartureDate;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
  @Column(name = "expected_arrival_at_place_of_delivery_start_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryStartDate;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
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
  @PseudoEnum(value = "communicationchannelqualifier.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String communicationChannelCode;

  @Column(name = "is_equipment_substitution_allowed")
  private Boolean isEquipmentSubstitutionAllowed;

  @PseudoEnum(value = "currencycodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  @Column(name = "declared_value_currency_code", length = 3)
  private String declaredValueCurrency;

  @Column(name = "declared_value")
  private Float declaredValue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vessel_id")
  private Vessel vessel;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location placeOfIssue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;

  @Column(name = "terms_and_conditions", length = 35)
  private String termsAndConditions;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<@Valid Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "list_order")
  private List<@Valid RequestedEquipmentGroup> requestedEquipments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<@Valid DocumentParty> documentParties;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", cascade = CascadeType.ALL, orphanRemoval = true)
  @Size(min = 2, message = "Must have at least two shipment locations", groups = AsyncShipperProvidedDataValidation.class)
  @NotNull(groups = AsyncShipperProvidedDataValidation.class) // -- for some reason this triggers, when shipmentLocations != null!?
  private Set<@Valid ShipmentLocation> shipmentLocations;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "list_order")
  private List<@Valid ShipmentCutOffTime> shipmentCutOffTimes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "voyage_id")
  private Voyage voyage;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany
  @JoinTable(
    name = "shipment_carrier_clauses",
    joinColumns = {@JoinColumn(name = "booking_data_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "carrier_clause_id", referencedColumnName = "id")})
  private Set<CarrierClause> carrierClauses = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData")
  private Set<ShipmentTransport> shipmentTransports = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingData", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "list_order")
  private List<ConfirmedEquipment> confirmedEquipments = new ArrayList<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "bookingDataID")
  private Set<Charge> charges = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OrderColumn(name = "list_order")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_data_id")
  private List<AdvanceManifestFiling> advanceManifestFilings = new ArrayList<>();

  public void assignRequestedEquipment(@NotNull List<RequestedEquipmentGroup> requestedEquipments) {
    this.requestedEquipments = Objects.requireNonNullElseGet(this.requestedEquipments, ArrayList::new);
    this.requestedEquipments.clear();
    this.requestedEquipments.addAll(requestedEquipments);
    for (var re : requestedEquipments) {
      re.setBookingData(this);
    }
  }

  public void assignConfirmedEquipments(@NotNull List<ConfirmedEquipment> confirmedEquipments) {
    this.confirmedEquipments = Objects.requireNonNullElseGet(this.confirmedEquipments, ArrayList::new);
    this.confirmedEquipments.clear();
    this.confirmedEquipments.addAll(confirmedEquipments);
    for (var e : confirmedEquipments) {
      e.setBookingData(this);  // For cascade to work properly
    }
  }

  public void assignShipmentCutOffTimes(@NotNull List<ShipmentCutOffTime> shipmentCutOffTimes) {
    this.shipmentCutOffTimes = Objects.requireNonNullElseGet(this.shipmentCutOffTimes, ArrayList::new);
    this.shipmentCutOffTimes.clear();
    this.shipmentCutOffTimes.addAll(shipmentCutOffTimes);
    for (var t : shipmentCutOffTimes) {
      t.setBookingData(this);  // For cascade to work properly
    }
  }

  public void assignAdvanceManifestFiling(@NotNull List<AdvanceManifestFiling> advanceManifestFilings) {
    this.advanceManifestFilings = Objects.requireNonNullElseGet(this.advanceManifestFilings, ArrayList::new);
    this.advanceManifestFilings.clear();
    this.advanceManifestFilings.addAll(advanceManifestFilings);
    for (var e : advanceManifestFilings) {
      e.setBookingData(this);  // For cascade to work properly
    }
  }

  public void assignTermsAndConditions(@NotNull String termsAndConditions) {
    this.termsAndConditions = termsAndConditions;
  }

  public void assignCarrier(@NotNull Carrier carrier) {
    this.carrier = carrier;
  }
}
