package org.modsen.util.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.modsen.util.validator.FileEmptinessValidator;
import org.modsen.util.constant.ExceptionConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileEmptinessValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFile {

    String message() default ExceptionConstant.EMPTY_FILE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
