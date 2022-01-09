package service.validation;

import persistence.repo.CommentRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommentExistsValidator implements ConstraintValidator<CommentExists, Integer> {
    @Inject CommentRepository commentRepo;

    @Override
    public void initialize(CommentExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (commentRepo.findById(value) != null);
    }
}
