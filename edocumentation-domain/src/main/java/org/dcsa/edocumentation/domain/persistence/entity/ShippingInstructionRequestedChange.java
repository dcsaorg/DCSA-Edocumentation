package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

import jakarta.validation.ConstraintViolation;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "si_requested_change")
public class ShippingInstructionRequestedChange {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "path", length = 500)
    private String path;

    @Column(name = "message", length = 500)
    private String message;

    public static ShippingInstructionRequestedChange fromConstraintViolation(ConstraintViolation<ShippingInstruction> violation) {
        return ShippingInstructionRequestedChange.builder()
                .path(violation.getPropertyPath().toString())
                .message(violation.getMessage())
                .build();
    }
}
