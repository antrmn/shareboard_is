package service.validation;


import persistence.model.Ban;
import persistence.repo.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BanExistsValidator implements ConstraintValidator<BanExists, Integer> {
    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(BanExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (genericRepository.findById(Ban.class, value) != null);
    }
}
