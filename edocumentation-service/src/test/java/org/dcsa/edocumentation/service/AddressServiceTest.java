package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.AddressDataFactory;
import org.dcsa.edocumentation.domain.persistence.AddressRepository;
import org.dcsa.edocumentation.service.mapping.AddressMapper;
import org.dcsa.edocumentation.service.util.EnsureResolvable.ResolvedEntity;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
  @Mock private AddressRepository addressRepository;
  @Mock private AddressMapper addressMapper;

  @InjectMocks private AddressService addressService;

  @BeforeEach
  public void resetMocks() {
    reset(addressRepository, addressMapper);
  }

  @Test
  public void testNull() {
    assertNull(addressService.ensureResolvable(null));
  }

  @Test
  public void testAddressMatched() {
    // Setup
    AddressTO addressTO = AddressDataFactory.addressTO();
    Address address = AddressDataFactory.addressWithId();

    when(addressMapper.toDAO(addressTO)).thenReturn(AddressDataFactory.addressWithoutId());
    when(addressRepository.findAll(any(Example.class))).thenReturn(List.of(address));

    // Execute
    ResolvedEntity<Address> actual = addressService.ensureResolvableToResolvedEntity(addressTO);

    // Verify
    assertEquals(address, actual.entity());
    assertFalse(actual.isNew());
    verify(addressMapper).toDAO(addressTO);
    verify(addressRepository).findAll(any(Example.class));
    verify(addressRepository, never()).save(any(Address.class));
  }

  @Test
  public void testAddressNotMatched() {
    // Setup
    AddressTO addressTO = AddressDataFactory.addressTO();
    Address addressWithId = AddressDataFactory.addressWithId();
    Address addressWithoutId = AddressDataFactory.addressWithoutId();

    when(addressMapper.toDAO(addressTO)).thenReturn(addressWithoutId);
    when(addressRepository.findAll(any(Example.class))).thenReturn(Collections.emptyList());
    when(addressRepository.save(any(Address.class))).thenReturn(addressWithId);

    // Execute
    ResolvedEntity<Address> actual = addressService.ensureResolvableToResolvedEntity(addressTO);

    // Verify
    assertEquals(addressWithId, actual.entity());
    assertTrue(actual.isNew());
    verify(addressMapper).toDAO(addressTO);
    verify(addressRepository).findAll(any(Example.class));
    verify(addressRepository).save(addressWithoutId);
  }
}
