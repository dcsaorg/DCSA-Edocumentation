package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.util.EnsureResolvable;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService extends EnsureResolvable<EquipmentTO, Equipment> {
  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  private final ExampleMatcher exampleMatcher =
      ExampleMatcher.matchingAll().withIncludeNullValues().withIgnorePaths("equipmentReference");

  @Override
  @Transactional
  public <C> C ensureResolvable(EquipmentTO equipmentTO, BiFunction<Equipment, Boolean, C> mapper) {

    if (equipmentTO == null) {
      return mapper.apply(null, false);
    }

    Equipment equipment = equipmentMapper.toDAO(equipmentTO);

    return ensureResolvable(
        equipmentRepository.findAll(Example.of(equipment, exampleMatcher)),
        () -> equipmentRepository.saveAndFlush(equipment),
        mapper);
  }

  public Map<String, Equipment> verifyCarrierOwnedEquipmentsExists(
      Set<String> allEquipmentReferences) {
    Map<String, Equipment> equipments =
        equipmentRepository.findByEquipmentReferences(allEquipmentReferences).stream()
            .collect(Collectors.toMap(Equipment::getEquipmentReference, Function.identity()));

    // Check that we could find them all
    List<String> notFoundRequestedEquipmentReferences =
        allEquipmentReferences.stream().filter(ref -> !equipments.containsKey(ref)).toList();
    if (!notFoundRequestedEquipmentReferences.isEmpty()) {
      throw ConcreteRequestErrorMessageException.notFound(
          "Could not find the following equipmentReferences in equipments: "
              + notFoundRequestedEquipmentReferences);
    }
    return equipments;
  }
}
