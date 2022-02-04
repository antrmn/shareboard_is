package service.validation;

import persistence.model.Section;
import persistence.repo.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionExistsByIdValidator implements ConstraintValidator<SectionExistsById, Integer> {
    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(SectionExistsById constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return (genericRepository.findById(Section.class, id) != null);
    }
}
