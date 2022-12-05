package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
  List<Equipment> findByEquipmentReferenceIn(Collection<String> equipmentReferences);
}
