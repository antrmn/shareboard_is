package service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameFormatValidator implements ConstraintValidator<UsernameFormat, String> {

    @Override
    public void initialize(UsernameFormat userExists) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return (username.length() <= 30 && username.length() >= 3) && username.matches("^[\\w\\-]+$");
    }
}