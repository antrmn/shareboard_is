package domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * La password deve avere un formato valido
 */
@Size(min=3, max=255)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface PasswordFormat {
    String message() default "Formato password non valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}