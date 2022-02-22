package service.validation;

import persistence.repo.SectionRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionExistsByNameValidator implements ConstraintValidator<SectionExists, String> {
    @Inject SectionRepository sectionRepo;

    @Override
    public void initialize(SectionExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null) return true;
        return (sectionRepo.getByName(value) != null);
    }
}
