package service.validation;

import persistence.repo.BanRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BanExistsValidator implements ConstraintValidator<BanExists, Integer> {
    @Inject BanRepository banRepo;

    @Override
    public void initialize(BanExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (banRepo.findById(value) != null);
    }
}
