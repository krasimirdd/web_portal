package com.kdimitrov.edentist.common.model.annotation;


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

    String MESSAGE = "The date must be in the workable hours!";

    String message() default MESSAGE;

    String date();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
