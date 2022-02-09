package service.validation;

import persistence.repo.UserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UserExistsByName, String> {
    @Inject
    UserRepository userRepository;


    @Override
    public void initialize(UserExistsByName userExists) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.getByEmail(email) == null;
    }
}
