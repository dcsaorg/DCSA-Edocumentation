package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UtilizedTransportEquipmentService {

  private final EquipmentService equipmentService;
  private final ActiveReeferSettingsService activeReeferSettingsService;

  private final UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;
  private final RequestedEquipmentGroupRepository requestedEquipmentGroupRepository;

  private final UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;

  @Transactional(Transactional.TxType.MANDATORY)
  public Map<String, UtilizedTransportEquipment> createUtilizedTransportEquipment(
      Collection<UtilizedTransportEquipmentTO> utilizedTransportEquipmentTOs) {

    Map<String, Equipment> equipmentMap = equipmentService.resolveEquipments(
      utilizedTransportEquipmentTOs, UtilizedTransportEquipmentTO::isShipperOwned, this::extractEquipment);

    return utilizedTransportEquipmentTOs.stream()
      .collect(Collectors.toMap(UtilizedTransportEquipmentTO::extractEquipmentReference, ute -> saveUTE(ute, equipmentMap)));
  }

  private UtilizedTransportEquipment saveUTE(UtilizedTransportEquipmentTO to, Map<String, Equipment> equipmentMap) {
    return utilizedTransportEquipmentRepository.save(utilizedTransportEquipmentMapper.toDAO(to, equipmentMap.get(to.extractEquipmentReference())));
  }

  private Stream<EquipmentTO> extractEquipment(UtilizedTransportEquipmentTO to) {
    return Stream.of(to.isShipperOwned() ?
      to.equipment() :
      EquipmentTO.builder().equipmentReference(to.equipmentReference()).build());
  }
}
