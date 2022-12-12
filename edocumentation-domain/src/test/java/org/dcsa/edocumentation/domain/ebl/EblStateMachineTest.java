package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
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
    ShippingInstruction shippingInstruction = ShippingInstruction.builder().build();
    TransportDocument transportDocument = TransportDocument.builder().shippingInstruction(shippingInstruction).build();
    shippingInstruction.receive();
    Assertions.assertEquals(RECE, shippingInstruction.getDocumentStatus());
    Assertions.assertTrue(shippingInstruction.isPendingUpdateSupported());
    shippingInstruction.pendingUpdate("Please fix foo!");
    Assertions.assertEquals(PENU, shippingInstruction.getDocumentStatus());
    shippingInstruction.receive();
    Assertions.assertEquals(RECE, shippingInstruction.getDocumentStatus());
    transportDocument.draft();
    Assertions.assertEquals(DRFT, shippingInstruction.getDocumentStatus());

    // Verify resume
    shippingInstruction = ShippingInstruction.builder().documentStatus(DRFT).build();
    Assertions.assertEquals(DRFT, shippingInstruction.getDocumentStatus());
    Assertions.assertTrue(shippingInstruction.isPendingApprovalSupported());
    shippingInstruction.pendingApproval("We have provided foo, please approve the draft TRD");
    Assertions.assertEquals(PENA, shippingInstruction.getDocumentStatus());
    shippingInstruction.approve();
    Assertions.assertEquals(APPR, shippingInstruction.getDocumentStatus());
    shippingInstruction.issue();
    Assertions.assertEquals(ISSU, shippingInstruction.getDocumentStatus());
  }

  @Test
  void testBasicAmendmentFlow() {
    ShippingInstruction shippingInstruction = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .build();
    TransportDocument transportDocument = TransportDocument.builder().shippingInstruction(shippingInstruction).build();
    shippingInstruction.receive();
    Assertions.assertEquals(RECE, shippingInstruction.getDocumentStatus());
    transportDocument.draft();
    Assertions.assertEquals(DRFT, shippingInstruction.getDocumentStatus());
    shippingInstruction.approve();
    Assertions.assertEquals(APPR, shippingInstruction.getDocumentStatus());
    shippingInstruction.issue();
    Assertions.assertEquals(ISSU, shippingInstruction.getDocumentStatus());

    shippingInstruction = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .documentStatus(DRFT)
      .build();
    Assertions.assertEquals(DRFT, shippingInstruction.getDocumentStatus());
    shippingInstruction.approve();
    Assertions.assertEquals(APPR, shippingInstruction.getDocumentStatus());
  }


  @Test
  void testBasicAmendmentFlowRestricted() {
    ShippingInstruction shippingInstruction = ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID())
      .build();
    TransportDocument transportDocument = TransportDocument.builder().shippingInstruction(shippingInstruction).build();
    shippingInstruction.receive();
    Assertions.assertEquals(RECE, shippingInstruction.getDocumentStatus());

    // RECE must go directly to DRFT in the amendment flow (which is weird because there is no
    // reject state either... >.>)
    Assertions.assertFalse(shippingInstruction.isPendingUpdateSupported());
    Assertions.assertThrows(InternalServerErrorException.class, () -> shippingInstruction.pendingUpdate("Not possible"));
    // The error should not have made it change state
    Assertions.assertEquals(RECE, shippingInstruction.getDocumentStatus());

    transportDocument.draft();
    // DRFT must go directly to APPR in the amendment flow.
    Assertions.assertFalse(shippingInstruction.isPendingApprovalSupported());
    Assertions.assertThrows(InternalServerErrorException.class, () -> shippingInstruction.pendingApproval("Not possible"));
    // Again, it retains the current state on invalid transitions.
    Assertions.assertEquals(DRFT, shippingInstruction.getDocumentStatus());


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
    List<Function<EblDocumentStatus, ShippingInstruction>> shiInitializers = List.of(
      (s) -> ShippingInstruction.builder().documentStatus(s).build(),
      (s) -> ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID()).documentStatus(s).build()
    );
    List<Function<EblDocumentStatus, TransportDocument>> trdInitializers = List.of(
      (s) -> TransportDocument.builder().shippingInstruction(ShippingInstruction.builder().documentStatus(s).build()).build(),
      (s) -> TransportDocument.builder().shippingInstruction(ShippingInstruction.builder().amendmentToTransportDocument(UUID.randomUUID()).documentStatus(s).build()).build()
    );

    for (var initializer : shiInitializers) {
      ShippingInstruction stateMachine = initializer.apply(VOID);
      Assertions.assertThrows(ConflictException.class, stateMachine::receive);
      Assertions.assertThrows(ConflictException.class, () -> stateMachine.pendingUpdate("Should throw"));

      Assertions.assertThrows(ConflictException.class, () -> stateMachine.pendingApproval("Should throw"));
      Assertions.assertThrows(ConflictException.class, stateMachine::approve);
      Assertions.assertThrows(ConflictException.class, stateMachine::issue);
      Assertions.assertThrows(ConflictException.class, stateMachine::surrender);
      Assertions.assertThrows(ConflictException.class, stateMachine::voidDocument);
    }
    for (var initializer : trdInitializers) {
      TransportDocument stateMachine = initializer.apply(VOID);
      Assertions.assertThrows(ConflictException.class, stateMachine::draft);
    }
  }
}
