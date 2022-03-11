package model.validation;

import model.entity.User;
import model.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Inject GenericRepository genericRepository;

    @Override
    public void initialize(UniqueUsername userExists) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(username == null) return true;
        return genericRepository.findByNaturalId(User.class, username) == null;
    }
}