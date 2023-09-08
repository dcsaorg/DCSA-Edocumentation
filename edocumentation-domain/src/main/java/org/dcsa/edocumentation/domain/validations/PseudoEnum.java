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
   * Name of the CSV dataset (usually "somename.csv")
   *
   * <p>The application must provide `validations/{value}` in its class path (usually via
   * `src/main/resources/validations/{value}`). That file will be parsed as a CSV file and
   * used to determine the values that makes up the pseudo enum.</p>
   */
  String value();

  /**
   * Name of the column.
   *
   * <p>If omitted (or blank), the first column is used by default.</p>
   */
  String column() default "";

  String message() default "This attribute is not used (but required by the Validation API)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
