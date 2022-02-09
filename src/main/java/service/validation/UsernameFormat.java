package service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameFormatValidator.class)
@Documented
public @interface UsernameFormat {
    String message() default "Formato username non valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}