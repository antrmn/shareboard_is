package domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Il formato del nome utente deve essere valido
 */
@Pattern(regexp = "^[\\w\\-]+$")
@Size(min=3, max=30)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface UsernameFormat {
    String message() default "Formato username non valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}