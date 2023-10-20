package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.ConstraintViolation;
import lombok.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "bkg_requested_change")
public class BookingRequestedChange {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "path", length = 500)
    private String path;

    @Column(name = "message", length = 500)
    private String message;

    public static BookingRequestedChange fromConstraintViolation(ConstraintViolation<BookingRequest> violation) {
        return BookingRequestedChange.builder()
                .path(violation.getPropertyPath().toString())
                .message(violation.getMessage())
                .build();
    }

}
