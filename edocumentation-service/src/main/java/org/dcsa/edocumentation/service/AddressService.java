package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.AddressRepository;
import org.dcsa.edocumentation.service.mapping.AddressMapper;
import org.dcsa.edocumentation.service.util.ResolvedEntity;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;

  private final ExampleMatcher exampleMatcher =
    ExampleMatcher.matchingAll().withIncludeNullValues().withIgnorePaths("id");

  /**
   * Ensures that an address is resolvable. Will create an Address if no matching Addresses are found.
   * if the input is null will return a ResolvedAddress containing a null Address.
   */
  public ResolvedEntity<Address> ensureResolvable(AddressTO addressTO) {
    if (addressTO == null) {
      return new ResolvedEntity<>(null, false);
    }

    Address mappedAddress = addressMapper.toDAO(addressTO);
    return addressRepository.findAll(Example.of(mappedAddress, exampleMatcher)).stream()
      .findFirst()
      .map(address -> new ResolvedEntity<>(address, false))
      .orElseGet(() -> new ResolvedEntity<>(addressRepository.save(mappedAddress), true));
  }
}
