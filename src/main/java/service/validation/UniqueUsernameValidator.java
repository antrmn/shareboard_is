package service.validation;

import persistence.repo.UserRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Inject
    UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername userExists) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        boolean check;
        try{
            check = userRepository.getByName(username) == null;
        }catch (NoResultException e){
            check = true;
        }
        return check;
    }
}