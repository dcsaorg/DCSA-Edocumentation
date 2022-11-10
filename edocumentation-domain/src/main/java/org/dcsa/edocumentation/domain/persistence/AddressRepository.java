package org.dcsa.edocumentation.domain.persistence;

import org.dcsa.skernel.domain.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> { }
