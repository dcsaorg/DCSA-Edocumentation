package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that a given UtilizedTransportEquipment contains a tareWeight and weightUnit for shipper owned equipment
 *
 * This constraint validation ensures that the UtilizedTransportEquipment is valid
 *
 * <ol>
 *     <li>Equipment Tare weight is set of shipper owned containers (isShipperOwned is true)</li>
 *     <li>Equipment Weight unit is set of shipper owned containers (isShipperOwned is true)</li>
 * </ol>
 *
 */
@Target({TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UtilizedTransportEquipmentValidator.class)
public @interface ValidUtilizedTransportEquipment {

  boolean allowNull() default false;

  String message() default "must be a valid UtilizedTransportEquipment";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
