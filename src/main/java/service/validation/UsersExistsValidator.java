package service.validation;

import persistence.model.User;
import persistence.repo.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsersExistsValidator implements ConstraintValidator<UserExists, Integer> {
    @Inject GenericRepository genericRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return genericRepository.findById(User.class, id) != null;
    }
}
