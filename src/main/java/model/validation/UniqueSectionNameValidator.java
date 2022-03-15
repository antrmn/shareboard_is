package model.validation;

import model.entity.Section;
import model.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueSectionNameValidator implements ConstraintValidator<UniqueSection, String> {
    @Inject GenericRepository genericRepository;


    @Override
    public void initialize(UniqueSection uniqueSection) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name==null) return true;
        return genericRepository.findByNaturalId(Section.class, name) == null;
    }
}
