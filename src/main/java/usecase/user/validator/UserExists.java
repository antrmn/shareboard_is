package usecase.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UsersExistsByIdValidator.class, UserExistsByNameValidator.class})
@Documented
public @interface UserExists {
    String message() default "User must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
