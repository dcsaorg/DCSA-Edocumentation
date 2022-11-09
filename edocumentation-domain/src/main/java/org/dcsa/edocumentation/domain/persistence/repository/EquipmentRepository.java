package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
  @Query("FROM Equipment WHERE equipmentReference in (:equipmentReferences)")
  List<Equipment> findByEquipmentReferences(Set<String> equipmentReferences);
}
