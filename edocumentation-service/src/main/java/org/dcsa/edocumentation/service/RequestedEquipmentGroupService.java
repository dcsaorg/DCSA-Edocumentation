package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RequestedEquipmentGroupService {
  private final EquipmentService equipmentService;
  private final RequestedEquipmentGroupRepository requestedEquipmentRepository;
  private final RequestedEquipmentGroupMapper requestedEquipmentGroupMapper;
  private final EquipmentMapper equipmentMapper;

  @Transactional(TxType.MANDATORY)
  public void createRequestedEquipments(Collection<RequestedEquipmentTO> requestedEquipments, Booking booking) {
    /* TODO rewrite this - RequestedEquipmentGroup are now linked to Equipment through UtilizedTransportEquipments
    if (requestedEquipments != null && !requestedEquipments.isEmpty()) {
      // Load all Equipments

      Map<String, Equipment> equipments =
        equipmentService.resolveEquipments(
          requestedEquipments,
          RequestedEquipmentTO::isShipperOwned,
          re -> equipmentMapper.toNonNullableDTOStream(re.equipmentReferences()));

      // Create and save RequestedEquipments
      requestedEquipmentRepository.saveAll(
        requestedEquipments.stream()
          .map(reTO -> {
            RequestedEquipment re = requestedEquipmentGroupMapper.toDAO(reTO, booking);
            if (reTO.equipmentReferences() == null) {
              return re;
            } else {
              return re.toBuilder()
                .equipments(reTO.equipmentReferences().stream()
                  .map(equipments::get)
                  .collect(Collectors.toSet())
                )
                .build();
            }
          })
          .toList()
      );
    }
     */
  }
}
