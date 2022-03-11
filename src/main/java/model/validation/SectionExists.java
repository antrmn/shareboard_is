package model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * L'identificativo deve corrispondere a una sezione esistente
 * @see SectionExistsByIdValidator
 * @see SectionExistsByNameValidator
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SectionExistsByNameValidator.class, SectionExistsByIdValidator.class})
@Documented
public @interface SectionExists {
    String message() default "Section must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
