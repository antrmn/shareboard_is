package service.validation;

import persistence.repo.SectionRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionExistsByIdValidator implements ConstraintValidator<SectionExistsById, Integer> {
    @Inject SectionRepository sectionRepo;

    @Override
    public void initialize(SectionExistsById constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return (sectionRepo.findById(id) != null);
    }
}
