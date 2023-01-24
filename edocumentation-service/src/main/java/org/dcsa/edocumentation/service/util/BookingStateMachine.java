package org.dcsa.edocumentation.service.util;

import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CANC;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CMPL;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CONF;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.PENC;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.PENU;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.RECE;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.REJE;

@AllArgsConstructor
public class BookingStateMachine extends AbstractStateMachine<BkgDocumentStatus> {
  private static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();

  private BkgDocumentStatus documentStatus;

  @Override
  protected DFADefinition<BkgDocumentStatus> getDfaDefinition() {
    return BOOKING_DFA_DEFINITION;
  }

  @Override
  protected BkgDocumentStatus getResumeFromState() {
    return documentStatus;
  }

  public static void validateTransition(BkgDocumentStatus from, BkgDocumentStatus to) {
    new BookingStateMachine(from).transitionTo(to);
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(BkgDocumentStatus currentState, BkgDocumentStatus successorState, CannotLeaveTerminalStateException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the booking because the booking is "
        + currentState.getValue().toLowerCase() + " (" + currentState.name() + ")",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(BkgDocumentStatus currentState, BkgDocumentStatus successorState, TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with documentStatus ("
        + currentState.name() + ").",
      e
    );
  }
}
