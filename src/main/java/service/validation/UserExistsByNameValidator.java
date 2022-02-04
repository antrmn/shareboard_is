package service.validation;

import persistence.repo.UserRepository;
import service.UserService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class UserExistsByNameValidator implements ConstraintValidator<UserExistsByName, String> {
    @Inject
    UserRepository userRepository;


    @Override
    public void initialize(UserExistsByName userExists) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.getByName(name) != null;
    }
}
