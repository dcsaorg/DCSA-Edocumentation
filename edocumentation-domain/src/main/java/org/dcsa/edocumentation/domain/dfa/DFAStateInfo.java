package org.dcsa.edocumentation.domain.dfa;

import java.util.Set;

public record DFAStateInfo<S>(boolean validState, Set<S> successorStates) {

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
