package org.dcsa.edocumentation.domain.validations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DocumentPartyPaperBLValidator.class)
public @interface DocumentPartyPaperBLValidation {
  String message() default "This attribute is not used (but required by the Validation API)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
