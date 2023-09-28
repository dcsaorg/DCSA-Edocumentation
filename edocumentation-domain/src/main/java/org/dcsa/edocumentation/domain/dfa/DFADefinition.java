package org.dcsa.edocumentation.domain.dfa;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DFADefinition<S extends Object> {

  protected final S initialState;
  protected final Map<S, DFAStateInfo<S>> stateInfo;

  public static <S extends Object> DFADefinitionBuilder<S> builder(S initialState) {
    return new DFADefinitionBuilder<>(initialState);
  }

  public DFA<S> fromInitialState() {
    return new DFA<>(this, initialState);
  }

  public DFA<S> resumeFromState(S s) {
    return new DFA<>(this, s);
  }

  public DFAStateInfo<S> getStateInfoForState(S s) {
    return this.stateInfo.get(s);
  }

}
