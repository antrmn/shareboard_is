package service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommentExistsValidator.class)
@Documented
public @interface CommentExists {
    String message() default "Comment must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
