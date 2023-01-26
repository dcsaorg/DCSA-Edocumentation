package org.dcsa.edocumentation.service.util;

import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.APPR;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.DRFT;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.ISSU;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.PENA;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.PENU;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.RECE;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.SURR;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.VOID;

@AllArgsConstructor
public class ShippingInstructionStateMachine extends AbstractStateMachine<EblDocumentStatus> {
  private static final DFADefinition<EblDocumentStatus> DEFAULT_EBL_DFA_DEFINITION =
    DFADefinition.builder(RECE)
      .nonTerminalState(RECE).successorNodes(PENU, DRFT)
      .nonTerminalState(PENU).successorNodes(RECE)
      .nonTerminalState(DRFT).successorNodes(PENA, APPR)
      .nonTerminalState(PENA).successorNodes(DRFT, APPR)
      .nonTerminalState(APPR).successorNodes(ISSU)
      .nonTerminalState(ISSU).successorNodes(SURR)
      .nonTerminalState(SURR).successorNodes(VOID)
      .terminalStates(VOID)
      .build();

  private static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION =
    DFADefinition.builder(RECE)
      .nonTerminalState(RECE).successorNodes(DRFT)
      .nonTerminalState(DRFT).successorNodes(APPR)
      .nonTerminalState(APPR).successorNodes(ISSU)
      .nonTerminalState(ISSU).successorNodes(SURR)
      .nonTerminalState(SURR).successorNodes(VOID)
      .terminalStates(VOID)
      .unreachableStates(PENU, PENA)
      .build();

  private DFADefinition<EblDocumentStatus> dfaDefinition;
  private EblDocumentStatus documentStatus;

  @Override
  protected DFADefinition<EblDocumentStatus> getDfaDefinition() {
    return dfaDefinition;
  }

  @Override
  protected EblDocumentStatus getResumeFromState() {
    return documentStatus;
  }

  public static void validateTransition(EblDocumentStatus from, EblDocumentStatus to, boolean amendment) {
    new ShippingInstructionStateMachine(amendment ? AMENDMENT_EBL_DFA_DEFINITION : DEFAULT_EBL_DFA_DEFINITION, from).transitionTo(to);
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(
    EblDocumentStatus currentState,
    EblDocumentStatus successorState,
    CannotLeaveTerminalStateException e) {
    // Special-case for terminal states, where we can generate quite nice sounding
    // messages such as "... because the document is void (VOID)".
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the ShippingInstruction as the ShippingInstruction is "
        + currentState.getValue().toLowerCase()
        + " ("
        + currentState.name()
        + ")",
      e);
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(
    EblDocumentStatus currentState,
    EblDocumentStatus successorState,
    TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the ShippingInstruction with documentStatus ("
        + currentState.name()
        + ").",
      e);
  }
}
