package org.dcsa.edocumentation.domain.persistence.entity;

import static org.dcsa.edocumentation.infra.enums.BookingStatus.*;

import jakarta.persistence.*;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.validations.*;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.infra.validation.StringEnumValidation;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@NamedEntityGraph(
  name = "graph.booking",
  attributeNodes = {
    @NamedAttributeNode(value = "bookingData", subgraph = "graph.booking-data"),
  })
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
public class Booking extends AbstractStateMachine<String> {

  private static final Set<String> CAN_BE_VALIDATED = Set.of(RECEIVED,
    PENDING_UPDATES_CONFIRMATION,
    PENDING_AMENDMENTS_APPROVAL);

  private static final DFADefinition<String> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECEIVED)
    .nonTerminalState(RECEIVED)
    .successorNodes(CONFIRMED, PENDING_UPDATE, REJECTED, // Carrier
      CANCELLED, PENDING_UPDATES_CONFIRMATION) // Shipper
    .nonTerminalState(PENDING_UPDATE)
    .successorNodes(PENDING_UPDATE, REJECTED, // Carrier
      PENDING_UPDATES_CONFIRMATION, CANCELLED) // Shipper
    .nonTerminalState(PENDING_UPDATES_CONFIRMATION)
    .successorNodes(CONFIRMED, PENDING_UPDATE, REJECTED, // Carrier
      CANCELLED) // Shipper
    .nonTerminalState(CONFIRMED)
    .successorNodes(PENDING_UPDATE, COMPLETED, DECLINED, // Carrier
      PENDING_AMENDMENTS_APPROVAL, CANCELLED) // Shipper
    .nonTerminalState(PENDING_AMENDMENTS_APPROVAL)
    .successorNodes(PENDING_UPDATE, CONFIRMED, DECLINED, // Carrier
      CANCELLED) // Shipper
    .terminalStates(COMPLETED, REJECTED, DECLINED, // Carrier
      CANCELLED) // Shipper
    .build();

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  private UUID id;

  @Column(name = "carrier_booking_request_reference", length = 100)
  private String carrierBookingRequestReference;

  @Column(name = "carrier_booking_reference", length = 35)
  private String carrierBookingReference;

  @Column(name = "booking_status")
  @StringEnumValidation(value = BookingStatus.class)
  private String bookingStatus;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
  @JoinColumn(name = "booking_data_id")
  @Setter(AccessLevel.PACKAGE)
  private BookingData bookingData;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "last_confirmed_booking_data_id")
  @Setter(AccessLevel.PACKAGE)
  private BookingData lastConfirmedBookingData;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
  @OrderColumn(name = "element_order")
  private List<BookingRequestedChange> requestedChanges;

  // TODO: Remove later (in DT-389)
  @CreatedDate
  @Column(name = "created_date_time")
  @Builder.Default
  protected OffsetDateTime bookingRequestCreatedDateTime = OffsetDateTime.now();

  // TODO: Remove later (in DT-389)
  @LastModifiedDate
  @Column(name = "updated_date_time")
  @Builder.Default
  protected OffsetDateTime bookingRequestUpdatedDateTime = OffsetDateTime.now();

  public void assignCarrierBookingReference(@NotNull String carrierBookingReference) {
    if (this.carrierBookingReference != null
      && !this.carrierBookingReference.equals(carrierBookingReference)) {

    }
    this.carrierBookingReference = carrierBookingReference;
  }

  public ValidationResult<String> asyncValidation(Validator validator) {
    List<String> validationErrors = new ArrayList<>();

    if (!CAN_BE_VALIDATED.contains(bookingStatus)) {
      throw new IllegalStateException("bookingStatus must be one of " + CAN_BE_VALIDATED);
    }
    if (this.requestedChanges == null) {
      this.requestedChanges = new ArrayList<>();
    }
    clearRequestedChanges();

    for (var violation : validator.validate(this.bookingData, AsyncShipperProvidedDataValidation.class)) {
      this.requestedChanges.add(BookingRequestedChange.fromConstraintViolation(violation));
      validationErrors.add(violation.getPropertyPath().toString() + ": " +  violation.getMessage());
    }

    // TODO: according to the latest Booking State Transition Diagram (STD),
    //  PENDING_UPDATES_CONFIRMATION should be replaced with CONFIRMED, but this change should be done together
    // with other STD-related changes so that new BOOKING_DFA_DEFINITION does not get broken
    var proposedStatus = validationErrors.isEmpty() ? PENDING_UPDATES_CONFIRMATION : PENDING_UPDATE;

    return new ValidationResult<>(proposedStatus, validationErrors);
  }

  private void clearRequestedChanges() {
    if (this.requestedChanges != null && !this.requestedChanges.isEmpty()) {
      this.requestedChanges.clear();
    }
  }


  /**
   * Transition the booking into its {@link BookingStatus#RECEIVED} state.
   */
  public void receive() {
    processTransition(RECEIVED, null, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#CANCELLED} state.
   */
  public void cancel(String reason) {
    processTransition(CANCELLED, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#REJECTED} state.
   */
  public void reject(String reason) {
    processTransition(REJECTED, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#REJECTED} state.
   */
  public void decline(String reason) {
    processTransition(DECLINED, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_UPDATE} state.
   */
  public void pendingUpdate(String reason) {
    processTransition(PENDING_UPDATE, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_UPDATES_CONFIRMATION} state
   * as a consequence of a shipper provided change
   */
  public void pendingUpdatesConfirmation(@NotNull BookingData newBookingData) {
    this.pendingUpdatesConfirmation();
    this.bookingData = newBookingData;
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_UPDATES_CONFIRMATION} state
   * as a consequence of a carrier side (partial) validation.
   */
  public void pendingUpdatesConfirmation() {
    processTransition(PENDING_UPDATES_CONFIRMATION, null, true);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_AMENDMENTS_APPROVAL} state.
   */
  public void pendingAmendmentsApproval(String reason) {
    processTransition(PENDING_AMENDMENTS_APPROVAL, reason, true);
  }

  /**
   * Transition the booking into its {@link BookingStatus#CONFIRMED} state.
   */
  public void confirm() {
    // TODO: Validate that all carrier provided attributes for confirming the booking has been given
    processTransition(CONFIRMED, null, true);
    this.lastConfirmedBookingData = this.bookingData;
  }

  /**
   * Transition the booking into its {@link BookingStatus#COMPLETED} state.
   */
  public void complete() {
    processTransition(COMPLETED, null, true);
  }

  @Override
  protected DFADefinition<String> getDfaDefinition() {
    return BOOKING_DFA_DEFINITION;
  }

  @Override
  protected String getResumeFromState() {
    return this.bookingStatus;
  }

  protected void processTransition(String bookingStatus, String reason, boolean clearRequestedChanges) {
    transitionTo(bookingStatus);
    this.bookingStatus = bookingStatus;
    if (carrierBookingRequestReference == null) {
      carrierBookingRequestReference = UUID.randomUUID().toString();
    }
    if (clearRequestedChanges) {
      this.clearRequestedChanges();
    }
  }

  @Override
  protected RuntimeException errorForAttemptToLeaveTerminalState(String currentState,
                                                                 String successorState,
                                                                 CannotLeaveTerminalStateException e) {
    log.error("Booking with id=" + (id != null ? id.toString() : "null") +" is in terminal state " + currentState +
      ", can not transition to state " + successorState);
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the booking because the booking status is '"
        + currentState + "'",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStateNotListedAsSuccessor(String currentState,
                                                                     String successorState,
                                                                     TargetStateIsNotSuccessorException e) {
    log.error("Booking with id=" + (id != null ? id.toString() : "null") +" is in state " + currentState +
      ", can not transition to unexpected state " + successorState);
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with the booking status '"
        + currentState + "'",
      e
    );
  }

}
