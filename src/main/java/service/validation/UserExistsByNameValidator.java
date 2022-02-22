package service.validation;

import persistence.repo.UserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistsByNameValidator implements ConstraintValidator<UserExists, String> {
    @Inject
    UserRepository userRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.getByName(name) != null;
    }
}
