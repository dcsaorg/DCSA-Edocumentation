package org.dcsa.edocumentation.service;

import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractSpecificationService<R extends JpaRepository<D, I> & JpaSpecificationExecutor<D>, D, I> {

  protected abstract R getRepository();

  protected <T> PagedResult<T> findViaComplexSpecificationWithLookup(
      Specification<D> specification,
      PageRequest pageRequest,
      Function<D, I> dToIDFunction,
      Function<D, T> toDTO) {
    var repository = getRepository();
    var page = repository.findAll(specification, pageRequest);

    var id2D = repository.findAllById(page.stream().map(dToIDFunction).toList())
      .stream()
      .collect(Collectors.toMap(dToIDFunction, Function.identity()));
    return new PagedResult<>(
      page.map(si -> id2D.get(dToIDFunction.apply(si)))
        .map(toDTO));
  }
}
