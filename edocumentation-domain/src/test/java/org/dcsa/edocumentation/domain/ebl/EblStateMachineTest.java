package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;


class EblStateMachineTest {

  @Test
  void testBasicDefaultFlow() {
    EBLStateMachine stateMachine = EBLStateMachine.fromInitialStateUsingDefaultFlow();
    Assertions.assertEquals(RECE, stateMachine.getCurrentStatus());
    stateMachine.pendingUpdate();
    Assertions.assertEquals(PENU, stateMachine.getCurrentStatus());
    stateMachine.receive();
    Assertions.assertEquals(RECE, stateMachine.getCurrentStatus());
    stateMachine.draft();
    Assertions.assertEquals(DRFT, stateMachine.getCurrentStatus());
    stateMachine.pendingApproval();
    Assertions.assertEquals(PENA, stateMachine.getCurrentStatus());
    stateMachine.approve();
    Assertions.assertEquals(APPR, stateMachine.getCurrentStatus());
    stateMachine.issue();
    Assertions.assertEquals(ISSU, stateMachine.getCurrentStatus());
  }

  @Test
  void testBasicAmendmentFlow() {
    EBLStateMachine stateMachine = EBLStateMachine.fromInitialStateUsingAmendmentFlow();
    Assertions.assertEquals(RECE, stateMachine.getCurrentStatus());
    stateMachine.draft();
    Assertions.assertEquals(DRFT, stateMachine.getCurrentStatus());
    stateMachine.approve();
    Assertions.assertEquals(APPR, stateMachine.getCurrentStatus());
    stateMachine.issue();
    Assertions.assertEquals(ISSU, stateMachine.getCurrentStatus());
  }


  @Test
  void testBasicAmendmentFlowRestricted() {
    EBLStateMachine stateMachine = EBLStateMachine.fromInitialStateUsingAmendmentFlow();
    Assertions.assertEquals(RECE, stateMachine.getCurrentStatus());
    // RECE must go directly to DRFT in the amendment flow (which is weird because there is no
    // reject state either... >.>)
    Assertions.assertThrows(InternalServerErrorException.class, stateMachine::pendingUpdate);
    // The error should not have made it change state
    Assertions.assertEquals(RECE, stateMachine.getCurrentStatus());
    stateMachine.draft();
    // DRFT must go directly to APPR in the amendment flow
    Assertions.assertThrows(InternalServerErrorException.class, stateMachine::pendingApproval);
    // Again, it retains the current state on invalid transitions.
    Assertions.assertEquals(DRFT, stateMachine.getCurrentStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    List<Function<EblDocumentStatus, EBLStateMachine>> initializers = List.of(
      EBLStateMachine::resumeFromStateUsingDefaultFlow,
      EBLStateMachine::resumeFromStateUsingAmendmentFlow
    );
    for (Function<EblDocumentStatus, EBLStateMachine> initializer : initializers) {
      EBLStateMachine stateMachine = initializer.apply(VOID);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::receive);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::pendingUpdate);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::draft);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::pendingApproval);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::approve);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::issue);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::surrender);
      Assertions.assertThrows(InternalServerErrorException.class, stateMachine::voidDocument);
    }
  }
}
