package org.dcsa.edocumentation.service.util;

import java.util.Optional;
import java.util.function.Function;

public record ResolvedEntity<E>(E entity, boolean isNew) {
  /**
   * Maps the Entity and returns the result. If the containing Entity is null the mappers will not be called.
   * @param ifExistingMapper ifExistingMapper is run if the Entity already existed in the database.
   * @param ifNewMapper ifNewMapper is run if the Entity was created or if it already existed but ifExistingMapper returns an empty Optional.
   */
  public <T> T map(Function<E, Optional<T>> ifExistingMapper, Function<E, T> ifNewMapper) {
    if (entity == null) {
      return null;
    } else if (isNew) {
      return ifNewMapper.apply(entity);
    } else {
      return ifExistingMapper.apply(entity).orElseGet(() -> ifNewMapper.apply(entity));
    }
  }

  /**
   * Returns an Optional with the entity.
   */
  public Optional<E> toOptional() {
    return Optional.ofNullable(entity);
  }
}
