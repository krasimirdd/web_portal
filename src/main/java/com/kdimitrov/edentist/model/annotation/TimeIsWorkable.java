package com.kdimitrov.edentist.model.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeIsWorkableValidator.class)
public @interface TimeIsWorkable {

    String message() default "The date must be in the future!";

    String date();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
