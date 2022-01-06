package service.validation;

import persistence.repo.UserRepository;
import service.UserService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class UsersExistsValidator implements ConstraintValidator<UserExists, Integer> {
    @Inject
    UserRepository userRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findById(id) != null;
    }
}
