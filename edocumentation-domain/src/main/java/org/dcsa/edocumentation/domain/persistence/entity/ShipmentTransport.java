package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportPlanStageCode;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.LocationSubType;
import org.dcsa.edocumentation.domain.validations.LocationValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_transport")
// Note: This is *NOT* a match to the DCSA IM definition for ShipmentTransport
// In the BKG/eBL, we can manage with a simplified version (consolidating multiple
// entities into one).
public class ShipmentTransport {

  @GeneratedValue
  @Id private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "shipment_id", nullable = false)
  private Shipment shipment;

  @Column(name = "transport_plan_stage_sequence_number")
  private Integer transportPlanStageSequenceNumber;

  @Column(name = "transport_plan_stage_code")
  @Enumerated(EnumType.STRING)
  private TransportPlanStageCode transportPlanStageCode;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "load_location_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO, LocationSubType.FACI},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location loadLocation;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "discharge_location_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO, LocationSubType.FACI},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location dischargeLocation;

  @Column(name = "planned_departure_date")
  private LocalDate plannedDepartureDate;

  @Column(name = "planned_arrival_date")
  private LocalDate plannedArrivalDate;

  @Column(name = "dcsa_transport_type")
  @PseudoEnum(value = "modeoftransportcodes.csv", column = "DCSA Transport Type")
  private String modeOfTransport;

  @Column(name = "vessel_imo_number")
  private String vesselIMONumber;

  @Column(name = "vessel_name")
  private String vesselName;

  @Column(name = "carrier_import_voyage_number")
  private String carrierImportVoyageNumber;

  @Column(name = "universal_import_voyage_reference")
  private String universalImportVoyageReference;

  @Column(name = "carrier_export_voyage_number")
  private String carrierExportVoyageNumber;

  @Column(name = "universal_export_voyage_reference")
  private String universalExportVoyageReference;

  @Column(name = "carrier_service_code")
  private String carrierServiceCode;

  @Column(name = "universal_service_reference")
  private String universalServiceReference;
}
