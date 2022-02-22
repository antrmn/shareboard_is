package service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostExistsValidator.class)
@Documented
public @interface PostExists {
    String message() default "Post must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
