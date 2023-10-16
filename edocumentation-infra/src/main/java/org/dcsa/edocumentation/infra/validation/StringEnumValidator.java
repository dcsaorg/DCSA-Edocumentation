package org.dcsa.edocumentation.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.infra.enums.StringEnum;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StringEnumValidator implements ConstraintValidator<StringEnumValidation, String> {

  private static final Map<String, Set<String>> CACHE = new ConcurrentHashMap<>();

  private Set<String> allowedValues;

  @Override
  public void initialize(StringEnumValidation constraintAnnotation) {
    allowedValues = findAllowedValues(constraintAnnotation.value());
  }

  private Set<String> findAllowedValues(Class<? extends StringEnum> stringEnumClass) {
    var cacheKey = stringEnumClass.getName();
    Set<String> values = CACHE.get(cacheKey);
    if (values != null) {
      return values;
    }

    List<Field> allFields = Arrays.asList(stringEnumClass.getDeclaredFields());

    List<String> stringEnumValues = allFields.stream()
      .filter(field -> field.getType().equals(String.class) &&
        java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
        java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
        java.lang.reflect.Modifier.isFinal(field.getModifiers()))
      .map(field -> {
        try {
          return (String)field.get(null);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      })
      .collect(Collectors.toList());

    values = new LinkedHashSet<>();
    for (var value : stringEnumValues) {
      values.add(value);
    }

    CACHE.put(cacheKey, Collections.unmodifiableSet(values));
    return values;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (null == value || this.allowedValues.contains(value)) {
      return true;
    }

    constraintValidatorContext.disableDefaultConstraintViolation();
    String values = "'" + String.join("', '", this.allowedValues) + "'";
    constraintValidatorContext.buildConstraintViolationWithTemplate(
      "Unexpected value '" + value + "', should have been one of: " + values
    ).addConstraintViolation();

    return false;
  }
}
