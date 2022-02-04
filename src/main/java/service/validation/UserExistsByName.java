package service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserExistsByNameValidator.class)
@Documented
public @interface UserExistsByName {
    String message() default "User must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}