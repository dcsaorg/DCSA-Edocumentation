package org.dcsa.edocumentation.domain.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dcsa.skernel.domain.persistence.entity.base.BaseTransportCall;
import org.hibernate.annotations.Formula;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transport_call")
public class TransportCall extends BaseTransportCall {
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "vessel_id")
  private Vessel vessel;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "import_voyage_id")
  private Voyage importVoyage;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "export_voyage_id")
  private Voyage exportVoyage;

  @Formula(
      "(  select transport_event.event_date_time  from transport_event "
          + "where transport_event.transport_call_id = id "
          + "AND transport_event.event_classifier_code = 'PLN' "
          + "AND transport_event.transport_event_type_code = 'ARRI' "
          + "ORDER  BY  transport_event.event_created_date_time  DESC limit 1 )")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private OffsetDateTime eventDateTimeArrival;

  @Formula(
      "(  select transport_event.event_date_time  from transport_event "
          + "where transport_event.transport_call_id = id "
          + "AND transport_event.event_classifier_code = 'PLN' "
          + "AND transport_event.transport_event_type_code = 'DEPA' "
          + "ORDER  BY  transport_event.event_created_date_time  DESC limit 1 )")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private OffsetDateTime eventDateTimeDeparture;
}
