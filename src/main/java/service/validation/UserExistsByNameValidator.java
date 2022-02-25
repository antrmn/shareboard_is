package service.validation;

import persistence.model.User;
import persistence.repo.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistsByNameValidator implements ConstraintValidator<UserExists, String> {
    @Inject GenericRepository genericRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name == null) return true;
        return genericRepository.findByNaturalId(User.class,name) != null;
    }
}
