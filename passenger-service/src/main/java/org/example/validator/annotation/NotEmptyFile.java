package org.example.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validator.FileEmptinessValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.example.utils.constant.ExceptionConstant.EMPTY_FILE_MESSAGE;

@Constraint(validatedBy = FileEmptinessValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFile {

    String message() default EMPTY_FILE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
