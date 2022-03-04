package usecase.section.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

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
