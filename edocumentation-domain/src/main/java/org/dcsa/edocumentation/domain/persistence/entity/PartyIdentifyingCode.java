package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSAResponsibleAgencyCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "party_identifying_code")
public class PartyIdentifyingCode {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;

  @Column(name = "code_list_name", length = 100)
  private String codeListName;

  @Column(name = "party_code", length = 100)
  private String partyCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "dcsa_responsible_agency_code")
  private DCSAResponsibleAgencyCode dcsaResponsibleAgencyCode;
}
