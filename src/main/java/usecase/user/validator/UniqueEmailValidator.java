package usecase.user.validator;

import domain.repository.UserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Inject
    UserRepository userRepository;


    @Override
    public void initialize(UniqueEmail userExists) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email==null) return true;
        return userRepository.getByEmail(email) == null;
    }
}
