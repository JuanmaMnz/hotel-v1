package com.hotel.Person.Annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {

    String message() default "La edad debe estar entre {min} y {max} años";

    int min() default 0;

    int max() default 110;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
