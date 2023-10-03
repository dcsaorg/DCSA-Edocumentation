package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestedEquipmentGroupService {
  private final EquipmentService equipmentService;
  private final ActiveReeferSettingsService activeReeferSettingsService;

  private final RequestedEquipmentGroupRepository requestedEquipmentGroupRepository;
  private final UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;

  private final RequestedEquipmentGroupMapper requestedEquipmentGroupMapper;
  private final EquipmentMapper equipmentMapper;

  @Transactional(TxType.MANDATORY)
  public void createRequestedEquipments(
    Collection<RequestedEquipmentTO> requestedEquipments,
    Booking booking,
    Commodity commodity
  ) {
    if (requestedEquipments != null && !requestedEquipments.isEmpty()) {
      // Resolve all Equipments
      equipmentService.resolveEquipments(
          requestedEquipments,
          RequestedEquipmentTO::isShipperOwned,
          equipmentMapper::toNonNullableDTOStream);

      // Create and save RequestedEquipments
      requestedEquipments.forEach(re -> requestedEquipmentGroupRepository.save(
        requestedEquipmentGroupMapper.toDAO(
          re,
          booking,
          activeReeferSettingsService.createActiveReeferSettings(re.activeReeferSettings()),
          commodity
        )
      ));
    }
  }
}
