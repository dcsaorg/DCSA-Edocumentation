package org.dcsa.edocumentation.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.dcsa.edocumentation.infra.enums.StringEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = StringEnumValidator.class)
public @interface StringEnumValidation {

  Class<? extends StringEnum> value();

  String message() default "{value} must be non-null";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
