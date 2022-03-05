package domain.validation;

import domain.entity.User;
import domain.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsersExistsByIdValidator implements ConstraintValidator<UserExists, Integer> {
    @Inject GenericRepository genericRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null) return true;
        return genericRepository.findById(User.class, id) != null;
    }
}
