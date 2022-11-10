package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.dcsa.skernel.errors.exceptions.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;


class EblStateMachineTest {

  @Test
  void testBasicDefaultFlow() {
    ShippingInstruction stateMachine = ShippingInstruction.builder().build();
    stateMachine.receive();
    Assertions.assertEquals(RECE, stateMachine.getDocumentStatus());
    Assertions.assertTrue(stateMachine.isPendingUpdateSupported());
    stateMachine.pendingUpdate("Please fix foo!");
    Assertions.assertEquals(PENU, stateMachine.getDocumentStatus());
    stateMachine.receive();
    Assertions.assertEquals(RECE, stateMachine.getDocumentStatus());
    stateMachine.draft();
    Assertions.assertEquals(DRFT, stateMachine.getDocumentStatus());

    // Verify resume
    stateMachine = ShippingInstruction.builder().documentStatus(DRFT).build();
    Assertions.assertEquals(DRFT, stateMachine.getDocumentStatus());
    Assertions.assertTrue(stateMachine.isPendingApprovalSupported());
    stateMachine.pendingApproval("We have provided foo, please approve the draft TRD");
    Assertions.assertEquals(PENA, stateMachine.getDocumentStatus());
    stateMachine.approve();
    Assertions.assertEquals(APPR, stateMachine.getDocumentStatus());
    stateMachine.issue();
    Assertions.assertEquals(ISSU, stateMachine.getDocumentStatus());
  }

  @Test
  void testBasicAmendmentFlow() {
    ShippingInstruction stateMachine = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .build();
    stateMachine.receive();
    Assertions.assertEquals(RECE, stateMachine.getDocumentStatus());
    stateMachine.draft();
    Assertions.assertEquals(DRFT, stateMachine.getDocumentStatus());
    stateMachine.approve();
    Assertions.assertEquals(APPR, stateMachine.getDocumentStatus());
    stateMachine.issue();
    Assertions.assertEquals(ISSU, stateMachine.getDocumentStatus());

    stateMachine = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .documentStatus(DRFT)
      .build();
    Assertions.assertEquals(DRFT, stateMachine.getDocumentStatus());
    stateMachine.approve();
    Assertions.assertEquals(APPR, stateMachine.getDocumentStatus());
  }


  @Test
  void testBasicAmendmentFlowRestricted() {
    ShippingInstruction stateMachine = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .build();
    stateMachine.receive();
    Assertions.assertEquals(RECE, stateMachine.getDocumentStatus());

    // RECE must go directly to DRFT in the amendment flow (which is weird because there is no
    // reject state either... >.>)
    Assertions.assertFalse(stateMachine.isPendingUpdateSupported());
    Assertions.assertThrows(InternalServerErrorException.class, () -> stateMachine.pendingUpdate("Not possible"));
    // The error should not have made it change state
    Assertions.assertEquals(RECE, stateMachine.getDocumentStatus());

    stateMachine.draft();
    // DRFT must go directly to APPR in the amendment flow.
    Assertions.assertFalse(stateMachine.isPendingApprovalSupported());
    Assertions.assertThrows(InternalServerErrorException.class, () -> stateMachine.pendingApproval("Not possible"));
    // Again, it retains the current state on invalid transitions.
    Assertions.assertEquals(DRFT, stateMachine.getDocumentStatus());


    ShippingInstruction amendmentStateMachine = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .documentStatus(DRFT)
      .build();
    // DRFT must go directly to APPR in the amendment flow - even after resume
    Assertions.assertFalse(amendmentStateMachine.isPendingApprovalSupported());
    Assertions.assertThrows(InternalServerErrorException.class, () -> amendmentStateMachine.pendingApproval("Not possible"));
    // Again, it retains the current state on invalid transitions.
    Assertions.assertEquals(DRFT, amendmentStateMachine.getDocumentStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    List<Function<EblDocumentStatus, ShippingInstruction>> initializers = List.of(
      (s) -> ShippingInstruction.builder().documentStatus(s).build(),
      (s) -> ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID()).documentStatus(s).build()
    );
    for (Function<EblDocumentStatus, ShippingInstruction> initializer : initializers) {
      ShippingInstruction stateMachine = initializer.apply(VOID);
      Assertions.assertThrows(ConflictException.class, stateMachine::receive);
      Assertions.assertThrows(ConflictException.class, () -> stateMachine.pendingUpdate("Should throw"));
      Assertions.assertThrows(ConflictException.class, stateMachine::draft);
      Assertions.assertThrows(ConflictException.class, () -> stateMachine.pendingApproval("Should throw"));
      Assertions.assertThrows(ConflictException.class, stateMachine::approve);
      Assertions.assertThrows(ConflictException.class, stateMachine::issue);
      Assertions.assertThrows(ConflictException.class, stateMachine::surrender);
      Assertions.assertThrows(ConflictException.class, stateMachine::voidDocument);
    }
  }
}
