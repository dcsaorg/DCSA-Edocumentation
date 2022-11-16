package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilizedTransportEquipmentService {

  private final UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;
  private final UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;
  private final EquipmentService equipmentService;

  @Transactional(Transactional.TxType.MANDATORY)
  public Map<String, UtilizedTransportEquipment> createUtilizedTransportEquipment(
      Collection<UtilizedTransportEquipmentTO> utilizedTransportEquipmentTOs) {

    Set<String> allCarrierOwnedEquipmentReferences =
        utilizedTransportEquipmentTOs.stream()
            .filter(Predicate.not(UtilizedTransportEquipmentTO::isShipperOwned))
            .map(UtilizedTransportEquipmentTO::equipment)
            .map(EquipmentTO::equipmentReference)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    equipmentService.verifyCarrierOwnedEquipmentsExists(allCarrierOwnedEquipmentReferences);

    utilizedTransportEquipmentTOs.forEach(
        utilizedTransportEquipmentTO ->
            equipmentService.ensureResolvable(utilizedTransportEquipmentTO.equipment()));

    return utilizedTransportEquipmentRepository
        .saveAll(
            utilizedTransportEquipmentTOs.stream()
                .map(utilizedTransportEquipmentMapper::toDAO)
                .toList())
        .stream()
        .collect(
            Collectors.toMap(
                utilizedTransportEquipment ->
                    utilizedTransportEquipment.getEquipment().getEquipmentReference(),
                utilizedTransportEquipment -> utilizedTransportEquipment));
  }
}
