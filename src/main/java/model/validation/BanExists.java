package model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * L'identificativo deve corrispondere a un ban esistente
 * @see BanExistsValidator
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BanExistsValidator.class)
@Documented
public @interface BanExists {
    String message() default "Ban must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
