package org.dcsa.edocumentation.domain.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = PseudoEnumValidator.class)
public @interface PseudoEnum {
  /**
   * Name of the dataset
   */
  String value();

  /**
   * Name of the column.
   *
   * <p>If omitted (or blank), the first column is used by default.</p>
   */
  String column() default "";

  String message() default "unsupported value for this pseudo-enum. Please check the {value} dataset for allowed values";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
