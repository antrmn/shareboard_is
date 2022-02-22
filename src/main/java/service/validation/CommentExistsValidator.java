package service.validation;

import persistence.model.Comment;
import persistence.repo.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommentExistsValidator implements ConstraintValidator<CommentExists, Integer> {
    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(CommentExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value==null) return true;
        return (genericRepository.findById(Comment.class, value) != null);
    }
}
