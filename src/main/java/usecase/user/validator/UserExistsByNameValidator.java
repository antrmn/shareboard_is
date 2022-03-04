package usecase.user.validator;

import domain.entity.User;
import domain.repository.GenericRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class UserExistsByNameValidator implements ConstraintValidator<UserExists, String> {
    @Inject GenericRepository genericRepository;


    @Override
    public void initialize(UserExists userExists) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name == null) return true;
        return genericRepository.findByNaturalId(User.class,name) != null;
    }
}
