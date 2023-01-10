package org.dcsa.edocumentation.transferobjects.validation;

import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UtilizedTransportEquipmentValidatorTest {

  @Mock
  private ConstraintValidatorContext constraintValidatorContext;

  @Test
  void UtilizedTransportEquipmentValidatorTest_testInvalidMissingTareWeight() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(true)
      .build();

    assertFalse(validate(utilizedTransportEquipmentTO));
  }

  @Test
  void UtilizedTransportEquipmentValidatorTest_testInvalidMissingWeightUnit() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .tareWeight(120.0)
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(true)
      .build();

    assertFalse(validate(utilizedTransportEquipmentTO));
  }

  @Test
  void UtilizedTransportEquipmentValidatorTest_testInvalidMissingWeightUnitAndTareWeight() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(true)
      .build();

    assertFalse(validate(utilizedTransportEquipmentTO));
  }

  @Test
  void UtilizedTransportEquipmentValidatorTest_testValidIsShipperOwned() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .tareWeight(130.0)
        .weightUnit(WeightUnit.LBR)
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(true)
      .build();

    assertTrue(validate(utilizedTransportEquipmentTO));
  }

  @Test
  void UtilizedTransportEquipmentValidatorTest_testValidIsNotShipperOwned() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(false)
      .build();

    assertTrue(validate(utilizedTransportEquipmentTO));
  }

  @Test
  void UtilizedTransportEquipmentValidatorTest_testValidIsNotShipperOwnedWithTareWeightAndWeightUnit() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .equipment(EquipmentTO.builder()
        .equipmentReference("equipmentReference")
        .tareWeight(394.0)
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(120.0)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .isShipperOwned(false)
      .build();

    assertTrue(validate(utilizedTransportEquipmentTO));
  }

  private boolean validate(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO) {
    UtilizedTransportEquipmentValidator uteValidator = new UtilizedTransportEquipmentValidator();
    UtilizedTransportEquipmentTO valueUnderTest = null;
    if (utilizedTransportEquipmentTO != null) {
      ValidUtilizedTransportEquipment uteAnno = utilizedTransportEquipmentTO.getClass().getAnnotation(ValidUtilizedTransportEquipment.class);
      uteValidator.initialize(uteAnno);
      valueUnderTest = utilizedTransportEquipmentTO;
    }
    return uteValidator.isValid(valueUnderTest, constraintValidatorContext);
  }


}
