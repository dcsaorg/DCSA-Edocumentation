package org.dcsa.edocumentation.domain.dfa;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class DFADefinition<S extends Enum<S>> {

  protected final S initialState;
  protected final Map<S, DFAStateInfo<S>> stateInfo;

  public static <S extends Enum<S>> DFADefinitionBuilder<S> builder(S initialState) {
    return new DFADefinitionBuilder<>(initialState);
  }

  public DFA<S> fromInitialState() {
    return new DFA<>(this, initialState);
  }

  public DFA<S> resumeFromState(S s) {
    return new DFA<>(this, s);
  }

  protected DFAStateInfo<S> getStateInfoForState(S s) {
    return this.stateInfo.get(s);
  }

  record DFAStateInfo<S>(boolean validState, Set<S> successorStates) {

    private static final DFAStateInfo<?> INVALID_STATE = new DFAStateInfo<>(false, Set.of());
    private static final DFAStateInfo<?> FINAL_STATE = new DFAStateInfo<>(true, Set.of());

    @SuppressWarnings("unchecked")
    public static <S extends Enum<S>> DFAStateInfo<S> unreachableState() {
      return (DFAStateInfo<S>) INVALID_STATE;
    }

    @SuppressWarnings("unchecked")
    public static <S extends Enum<S>> DFAStateInfo<S> terminalState() {
      return (DFAStateInfo<S>) FINAL_STATE;
    }

    public static <S extends Enum<S>> DFAStateInfo<S> successorStates(Set<S> s) {
      return new DFAStateInfo<>(true, Set.copyOf(s));
    }
  }
}
