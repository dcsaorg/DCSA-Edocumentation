package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestedEquipmentService {
  private final EquipmentRepository equipmentRepository;
  private final RequestedEquipmentRepository requestedEquipmentRepository;
  private final RequestedEquipmentMapper requestedEquipmentMapper;

  @Transactional(TxType.MANDATORY)
  public void createRequestedEquipments(Collection<RequestedEquipmentTO> requestedEquipments, Booking booking) {
    if (requestedEquipments != null && !requestedEquipments.isEmpty()) {
      // Load all Equipments
      Set<String> allEquipmentReferences = requestedEquipments.stream()
        .map(RequestedEquipmentTO::equipmentReferences)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
      Map<String, Equipment> equipments =
        equipmentRepository.findByEquipmentReferences(allEquipmentReferences).stream()
          .collect(Collectors.toMap(Equipment::getEquipmentReference, Function.identity()));

      // Check that we could find them all
      List<String> notFoundRequestedEquipmentReferences = allEquipmentReferences.stream()
        .filter(ref -> !equipments.containsKey(ref))
        .toList();
      if (!notFoundRequestedEquipmentReferences.isEmpty()) {
        throw ConcreteRequestErrorMessageException.notFound(
          "Could not find the following equipmentReferences in equipments: " + notFoundRequestedEquipmentReferences);
      }

      // Create and save RequestedEquipments
      requestedEquipmentRepository.saveAll(
        requestedEquipments.stream()
          .map(reTO -> {
            RequestedEquipment re = requestedEquipmentMapper.toDAO(reTO, booking);
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
  }
}
