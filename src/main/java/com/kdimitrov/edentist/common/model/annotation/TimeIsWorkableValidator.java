package com.kdimitrov.edentist.common.model.annotation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TimeIsWorkableValidator implements ConstraintValidator<TimeIsWorkable, Object> {

    private String fieldName;
    private String message;

    @Override
    public void initialize(TimeIsWorkable constraintAnnotation) {
        this.fieldName = constraintAnnotation.date();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid;

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        final String dateObj = (String) beanWrapper.getPropertyValue(fieldName);

        try {
            assert dateObj != null;
            int hour = LocalDateTime.parse(dateObj).getHour();
            int day = LocalDateTime.parse(dateObj).getDayOfWeek().getValue();
            valid = hour > 8 && hour < 20 && day != 6 && day != 7;
        } catch (AssertionError | DateTimeParseException e) {
            valid = false;
        }

        if (!valid) {
            context.
                    buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
