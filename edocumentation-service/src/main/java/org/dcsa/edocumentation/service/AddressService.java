package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.AddressRepository;
import org.dcsa.edocumentation.service.mapping.AddressMapper;
import org.dcsa.edocumentation.service.util.EnsureResolvable;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class AddressService extends EnsureResolvable<AddressTO, Address> {
  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;

  private final ExampleMatcher exampleMatcher =
    ExampleMatcher.matchingAll().withIncludeNullValues().withIgnorePaths("id");

  /**
   * Ensures that an address is resolvable. Will create an Address if no matching Addresses are found.
   * If the input is null will return the result of calling the mapper with (null, false).
   */
  @Override
  @Transactional
  public <C> C ensureResolvable(AddressTO addressTO, BiFunction<Address, Boolean, C> mapper) {
    if (addressTO == null) {
      return mapper.apply(null, false);
    }

    Address mappedAddress = addressMapper.toDAO(addressTO);
    return ensureResolvable(
      addressRepository.findAll(Example.of(mappedAddress, exampleMatcher)),
      () -> addressRepository.save(mappedAddress),
      mapper
    );
  }
}
