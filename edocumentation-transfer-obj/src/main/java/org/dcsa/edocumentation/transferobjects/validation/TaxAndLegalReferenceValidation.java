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
@Constraint(validatedBy = TaxAndLegalReferenceValidator.class)
public @interface TaxAndLegalReferenceValidation {

  String message() default "Invalid combination of Tax and Legal Reference type and country code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
