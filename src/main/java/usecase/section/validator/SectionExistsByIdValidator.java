package usecase.section.validator;

import domain.entity.Section;
import domain.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class SectionExistsByIdValidator implements ConstraintValidator<SectionExists, Integer> {
    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(SectionExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        if(id==null) return true;
        return (genericRepository.findById(Section.class, id) != null);
    }
}
