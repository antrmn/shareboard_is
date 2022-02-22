package service.validation;

import persistence.repo.SectionRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueSectionNameValidator implements ConstraintValidator<UniqueSection, String> {
    @Inject SectionRepository sectionRepository;


    @Override
    public void initialize(UniqueSection uniqueSection) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name==null) return true;
        return sectionRepository.getByName(name) == null;
    }
}
