package org.dcsa.edocumentation.infra.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dcsa.edocumentation.infra.enums.StringEnum;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class StringEnumValidatorTest {
  private ValidatorFactory validatorFactory;

  @BeforeAll
  public void createValidatorFactory() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
  }

  @AfterAll
  public void closeValidatorFactory() {
    validatorFactory.close();
  }

  @Test
  public void givenTestRecord_whenBookingStatusStringNullOrValid_thenNoViolation() {
    assertTrue(validate(new TestRecord(null, "PENDING UPDATE")).isEmpty());
  }

  @Test
  void givenTestRecord_whenBookingStatusStringInvalid_thenViolation() {
    var violations = validate(new TestRecord("RECEIVED", "DUMMY"));
    assertEquals(1, violations.size());
    var violation = violations.iterator().next();
    assertEquals("stringEnumB", violation.getPropertyPath().toString());
    assertEquals("Unexpected value 'DUMMY', should have been one of: 'RECEIVED', 'PENDING UPDATE', 'COMPLETED'",
      violation.getMessage());
  }


  private <T> Set<ConstraintViolation<T>> validate(T value) {
    return validatorFactory.getValidator().validate(value);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public class TestStatus extends StringEnum {
    public static final String RECEIVED = "RECEIVED";
    public static final String PENDING_UPDATE = "PENDING UPDATE";
    public static final String COMPLETED = "COMPLETED";
  }

  public record TestRecord(
    @StringEnumValidation(value=TestStatus.class)
    String stringEnumA,
    @StringEnumValidation(value=TestStatus.class)
    String stringEnumB
  ) { }
}

