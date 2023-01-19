package org.dcsa.edocumentation.domain.decoupled.repository;

import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("decoupledBookingRepository")
public interface BookingRepository extends JpaRepository<Booking, UUID> { }
