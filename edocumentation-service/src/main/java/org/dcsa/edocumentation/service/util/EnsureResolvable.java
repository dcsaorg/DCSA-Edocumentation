package org.dcsa.edocumentation.service.util;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Tiny base for services that needs to implement an ensureResolvable method.
 * @param <T> transferobject type
 * @param <E> entity type
 */
public abstract class EnsureResolvable<T, E> {
  public record ResolvedEntity<E>(E entity, boolean isNew) { }

  /**
   * Ensures that something is resolvable, creating new entities if needed.
   */
  @Transactional
  public E ensureResolvable(T t) {
    return ensureResolvable(t, (e, isNew) -> e);
  }

  /**
   * Ensures that something is resolvable, creating new entities if needed.
   * @return returns the resolved entity in a ResolvedEntity.
   */
  @Transactional
  public ResolvedEntity<E> ensureResolvableToResolvedEntity(T t) {
    return ensureResolvable(t, ResolvedEntity::new);
  }

  /**
   * Ensures that something is resolvable, creating new entities if needed using a custom mapper
   * to return the result.
   * If the input is null will return the result of calling the mapper with (null, false).
   */
  @Transactional
  public abstract <C> C ensureResolvable(T t, BiFunction<E, Boolean, C> mapper);

  /**
   * Ensures that something is resolvable, creating new entities if needed and using a custom mapper
   * to return the result.
   *
   * @param collection collection of potential candidates, if not empty uses the first entry.
   * @param creator creator of entities in case the collection is empty.
   * @param mapper custom mapper.
   */
  protected <C> C ensureResolvable(Collection<E> collection, Supplier<E> creator, BiFunction<E, Boolean, C> mapper) {
    return ensureResolvable(collection.stream().findFirst(), creator, mapper);
  }

  /**
   * Ensures that something is resolvable, creating new entities if needed and using a custom mapper
   * to return the result.
   *
   * @param candidate potential candidate, if not empty it is used
   * @param creator creator of entities in case the candidate is empty.
   * @param mapper custom mapper.
   */
  protected <C> C ensureResolvable(Optional<E> candidate, Supplier<E> creator, BiFunction<E, Boolean, C> mapper) {
    return candidate
      .map(entity -> mapper.apply(entity, false))
      .orElseGet(() -> mapper.apply(creator.get(), true));
  }
}
