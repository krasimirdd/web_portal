package com.kdimitrov.edentist.common.model.annotation;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateInFutureValidator implements ConstraintValidator<DateInFuture, Object> {

    private String fieldName;
    private String message;

    @Override
    public void initialize(DateInFuture constraintAnnotation) {
        this.fieldName = constraintAnnotation.date();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        final String dateObj = (String) beanWrapper.getPropertyValue(fieldName);

        boolean valid = Strings.isNotBlank(dateObj)
                && LocalDateTime.parse(dateObj).isAfter(LocalDateTime.now());

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
