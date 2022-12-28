package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UtilizedTransportEquipmentService {

  private final EquipmentService equipmentService;

  private final UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;

  private final UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;

  @Transactional(Transactional.TxType.MANDATORY)
  public Map<String, UtilizedTransportEquipment> createUtilizedTransportEquipment(
      Collection<UtilizedTransportEquipmentTO> utilizedTransportEquipmentTOs) {

    equipmentService.resolveEquipments(utilizedTransportEquipmentTOs, UtilizedTransportEquipmentTO::isShipperOwned, e -> Stream.of(e.equipment()));

    return utilizedTransportEquipmentRepository
        .saveAll(
            utilizedTransportEquipmentTOs.stream()
                .map(ute -> utilizedTransportEquipmentMapper.toDAO(ute, null))
                .toList())
        .stream()
        .collect(
            Collectors.toMap(
                utilizedTransportEquipment ->
                    utilizedTransportEquipment.getEquipment().getEquipmentReference(),
                utilizedTransportEquipment -> utilizedTransportEquipment));
  }

}
