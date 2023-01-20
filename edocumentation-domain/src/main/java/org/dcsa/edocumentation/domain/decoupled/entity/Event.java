package org.dcsa.edocumentation.domain.decoupled.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class Event {
  @Id
  @GeneratedValue
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  @Column(name = "event_date_time", nullable = false)
  private OffsetDateTime eventDateTime;

  @CreatedDate
  @Column(name = "event_created_date_time", nullable = false)
  private OffsetDateTime eventCreatedDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_classifier_code", nullable = false)
  private EventClassifierCode eventClassifierCode;
}
