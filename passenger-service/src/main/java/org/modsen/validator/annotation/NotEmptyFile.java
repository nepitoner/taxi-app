package org.modsen.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.modsen.util.constant.ExceptionConstant;
import org.modsen.validator.FileEmptinessValidator;

@Constraint(validatedBy = FileEmptinessValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFile {

    String message() default ExceptionConstant.EMPTY_FILE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
