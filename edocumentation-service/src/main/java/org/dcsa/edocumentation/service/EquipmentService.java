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
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EquipmentService extends EnsureResolvable<EquipmentTO, Equipment> {
  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  private final ExampleMatcher exampleMatcher =
      ExampleMatcher.matchingAll().withIncludeNullValues();

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

  public void verifyCarrierOwnedEquipmentsExists(
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
  }

  /**
   * Resolves equipments.
   * @param collection collection of TOs that are not necessarily EquipmentTOs themselves but which contains EquipmentTOs.
   * @param getCreateIfNotExists function to extract information on weather to create Equipments if they do not exist.
   * @param getEquipments function to extract the EquipmentTOs themselves as a Stream.
   * @return map of "equipmentReference → Equipment"
   */
  @Transactional
  public <T> Map<String, Equipment> resolveEquipments(Collection<T> collection, Predicate<T> getCreateIfNotExists, Function<T, Stream<EquipmentTO>> getEquipments) {
    if (collection == null || collection.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, EquipmentTO> allEquipmentTOs = new HashMap<>();
    Set<String> mustExist = new HashSet<>();
    Set<String> mustResolve = new HashSet<>();

    collection.forEach(to -> {
      boolean createIfNotExists = getCreateIfNotExists.test(to);
      getEquipments.apply(to).forEach(equipmentTO -> {
        String equipmentReference = equipmentTO.equipmentReference();
        if (allEquipmentTOs.containsKey(equipmentReference)) {
          throw ConcreteRequestErrorMessageException.invalidInput(
            "equipmentReference = '" + equipmentReference + "' is used more than once in List<"
              + to.getClass().getSimpleName() + ">");
        }
        allEquipmentTOs.put(equipmentReference, equipmentTO);
        if (createIfNotExists) {
          mustResolve.add(equipmentReference);
        } else {
          mustExist.add(equipmentReference);
        }
      });
    });

    if (allEquipmentTOs.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, Equipment> equipments =
      equipmentRepository.findByEquipmentReferences(allEquipmentTOs.keySet()).stream()
        .collect(Collectors.toMap(Equipment::getEquipmentReference, Function.identity()));

    mustExist.removeAll(equipments.keySet());
    if (!mustExist.isEmpty()) {
      throw ConcreteRequestErrorMessageException.notFound(
        "Could not find the following equipmentReferences in equipments: " + mustExist);
    }

    mustResolve.removeAll(equipments.keySet());
    mustResolve.forEach(reference -> {
      equipments.put(reference, equipmentRepository.save(equipmentMapper.toDAO(allEquipmentTOs.get(reference))));
    });
    return equipments;
  }
}
