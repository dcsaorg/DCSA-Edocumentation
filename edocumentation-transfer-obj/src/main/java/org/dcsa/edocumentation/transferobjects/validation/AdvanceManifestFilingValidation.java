package org.dcsa.edocumentation.transferobjects.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AdvanceManifestFilingValidator.class)
public @interface AdvanceManifestFilingValidation {

  String message() default "Invalid combination of manifest type code and country code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
