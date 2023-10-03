package org.dcsa.edocumentation.domain.dfa;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import java.util.*;

public class DFADefinitionBuilder<S extends Object> {


  private final Set<S> terminalStates;
  private final Set<S> unreachableStates;
  private final Map<S, Set<S>> successorStates;
  private final S initialState;

  DFADefinitionBuilder(S initialState) {
    if (initialState == null) {
      throw ConcreteRequestErrorMessageException.internalServerError("Initial state must not be null");
    }
    this.initialState = initialState;
    this.successorStates = new HashMap<>();
    this.terminalStates = new HashSet<>();
    this.unreachableStates = new HashSet<>();
  }

  /**
   * Explicitly flag the states as known but unreachable
   *
   * This is mostly useful for generating better error messages
   * (as code can tell it was not just forgotten but expected to
   * be unreachable).
   *
   * @param states States that are declared but will not be valid.
   * @return This builder object
   */
  public DFADefinitionBuilder<S> unreachableStates(S ... states) {
    for (S s : states) {
      ensureStateIsUnused(s);
      this.unreachableStates.add(s);
    }
    return this;
  }

  /**
   * Mark the states as a terminal state
   *
   * A terminal state is a state you cannot leave.
   *
   * @param states The states that are terminal (final)
   * @return This builder object
   */
  public DFADefinitionBuilder<S> terminalStates(S ... states) {
    for (S s : states) {
      ensureStateIsUnused(s);
      this.terminalStates.add(s);
    }
    return this;
  }

  /**
   * Mark the state as a non-terminal state
   *
   * @param s The state that is non-terminal
   * @return A state builder object where you can define the successors
   */
  public DFADefinitionNonTerminalStateBuilder nonTerminalState(S s) {
    ensureStateIsUnused(s);
    return new DFADefinitionNonTerminalStateBuilder(s);
  }

  private void ensureStateIsUnused(S s) {
    if (this.terminalStates.contains(s)) {
      throw ConcreteRequestErrorMessageException.internalServerError("State " + s.toString()
        + " is already declared as a terminal state");
    }
    if (this.unreachableStates.contains(s)) {
      throw ConcreteRequestErrorMessageException.internalServerError("State " + s.toString()
        + " is already declared as an unreachable state");
    }
    if (this.successorStates.containsKey(s)) {
      throw ConcreteRequestErrorMessageException.internalServerError("State " + s.toString()
        + " is already declared as a non-terminal state");
    }
  }

  public DFADefinition<S> build() {
    Map<S, DFAStateInfo<S>> stateInfo = new HashMap<>();
    for (S s : terminalStates) {
      stateInfo.put(s, DFAStateInfo.terminalState());
    }
    for (S s : unreachableStates) {
      stateInfo.put(s, DFAStateInfo.unreachableState());
    }
    for (Map.Entry<S, Set<S>> entry : successorStates.entrySet()) {
      stateInfo.put(entry.getKey(), DFAStateInfo.successorStates(entry.getValue()));
    }
    DFADefinition<S> definition = new DFADefinition<>(initialState, Map.copyOf(stateInfo));
    Set<S> expectedReachable = new HashSet<>();
    expectedReachable.addAll(terminalStates);
    expectedReachable.addAll(successorStates.keySet());
    verifyConsistency(definition, expectedReachable);
    return definition;
  }

  private void verifyConsistency(DFADefinition<S> definition, Set<S> expectedReachable) {
    Set<S> reachableStates = new HashSet<>();
    Set<S> pendingStates = new HashSet<>();
    int loopCounter = 0;
    // The implementation should visit every state at most once.  If it does not, then someone
    // introduced a bug that (likely) makes it loop forever.
    final int loopMax = definition.stateInfo.size();
    pendingStates.add(initialState);

    while (!pendingStates.isEmpty()) {
      S nextState = pendingStates.iterator().next();
      pendingStates.remove(nextState);
      reachableStates.add(nextState);
      DFAStateInfo<S> stateInfo = definition.getStateInfoForState(nextState);
      if (stateInfo == null) {
        throw ConcreteRequestErrorMessageException.internalServerError("It was possible to reach " + nextState.toString()
          + ", which has not been declared as a known state");
      }
      if (!stateInfo.validState()) {
        throw ConcreteRequestErrorMessageException.internalServerError("It was possible to reach " + nextState.toString()
          + ", which was explicitly declared as unreachable!");
      }
      stateInfo.successorStates().stream().filter(s -> !reachableStates.contains(s)).forEach(pendingStates::add);
      if (loopCounter++ > loopMax) {
        throw new IllegalStateException("Non-termination! Reachable " + reachableStates + " vs. pending " + pendingStates);
      }
    }
    if (!reachableStates.equals(expectedReachable)) {
      throw ConcreteRequestErrorMessageException.internalServerError("Inconsistent DFA: The following states are reachable "
        + expectedReachable + ", but we expected the set to be "
        + unreachableStates + " based on the input");
    }
  }

  @RequiredArgsConstructor
  public class DFADefinitionNonTerminalStateBuilder {
    private final S baseState;
    private final Set<S> successors = new HashSet<>();

    /**
     * Declare which states can follow from this non-terminal state.
     *
     * @param s The states that can follow from this state
     * @return The DFADefinitionBuilder
     */
    public DFADefinitionBuilder<S> successorNodes(S ... s) {
      if (s.length < 1) {
        throw ConcreteRequestErrorMessageException.internalServerError("There must be at least one successor for a non-terminal state");
      }
      successors.addAll(Arrays.asList(s));
      ensureStateIsUnused(baseState);
      successorStates.put(baseState, successors);
      return DFADefinitionBuilder.this;
    }
  }
}
