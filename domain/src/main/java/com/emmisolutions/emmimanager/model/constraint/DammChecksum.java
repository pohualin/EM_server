package com.emmisolutions.emmimanager.model.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DammChecksumConstraintValidator.class)
@Documented
public @interface DammChecksum {

    String message() default "The value is not valid: the Damm checksum is incorrect.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
