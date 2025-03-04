package org.modsen.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.modsen.validator.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Enum<?>[] enumConstants;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumConstants = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(enumConstants)
            .anyMatch(enumConstant -> enumConstant.name().equals(value));
    }

}
