package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommodityRequestedEquipmentLinkRepository extends JpaRepository<CommodityRequestedEquipmentLink, UUID> { }
