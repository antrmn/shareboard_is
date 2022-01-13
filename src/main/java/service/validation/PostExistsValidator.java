package service.validation;

import persistence.repo.PostRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostExistsValidator implements ConstraintValidator<PostExists, Integer> {
    @Inject PostRepository postRepo;

    @Override
    public void initialize(PostExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (postRepo.findById(value) != null);
    }
}
