package usecase.section.validator;

import domain.entity.Section;
import domain.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionExistsByNameValidator implements ConstraintValidator<SectionExists, String> {
    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(SectionExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null) return true;
        return (genericRepository.findByNaturalId(Section.class,value) != null);
    }
}
