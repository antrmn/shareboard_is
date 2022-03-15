package model.validation;

import model.entity.User;
import model.repository.GenericRepository;

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
