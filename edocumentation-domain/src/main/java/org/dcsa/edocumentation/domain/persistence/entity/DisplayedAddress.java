package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "displayed_address")
public class DisplayedAddress {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "document_party_id")
  private DocumentParty documentParty;

  @Size(max = 250)
  @Column(name = "address_line_text")
  private String addressLine;

  @NotNull
  @Column(name = "address_line_number")
  private Integer addressLineNumber;
}
