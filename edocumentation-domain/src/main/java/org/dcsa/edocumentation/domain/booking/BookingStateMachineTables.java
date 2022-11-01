package org.dcsa.edocumentation.domain.booking;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import java.util.Map;
import java.util.Set;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;

@UtilityClass
class BookingStateMachineTables {

  static final Map<BkgDocumentStatus, Set<BkgDocumentStatus>> BOOKING_STATE_TRANSITION_TABLE = buildStateTransitionTable();
  static final BkgDocumentStatus INITIAL_STATE = RECE;

  private static Set<BkgDocumentStatus> successorStates(BkgDocumentStatus ... statuses) {
    Set<BkgDocumentStatus> successors = Set.of(statuses);
    if (successors.isEmpty()) {
      // Fail-safe
      throw ConcreteRequestErrorMessageException.internalServerError("No successor states!? Please us terminalState instead");
    }
    if (successors.size() < statuses.length) {
      // Fail-safe
      throw ConcreteRequestErrorMessageException.internalServerError("Successor states contain duplicates!");
    }
    return successors;
  }

  // Strictly speaking, this method is unnecessary, but it makes the transition table
  // easier to read.
  private static Set<BkgDocumentStatus> terminalState() {
    return Set.of();
  }

  private static Map<BkgDocumentStatus, Set<BkgDocumentStatus>> buildStateTransitionTable() {
    return Map.of(
      RECE, successorStates(REJE, CANC, PENU, PENC, CONF),
      PENU, successorStates(REJE, CANC, PENU, PENC),
      PENC, successorStates(REJE, CANC, PENU, PENC, CONF),
      CONF, successorStates(REJE, CMPL, PENU, PENC),
      CANC, terminalState(),
      CMPL, terminalState(),
      REJE, terminalState()
    );
  }

}
