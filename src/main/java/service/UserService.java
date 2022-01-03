package service;

import persistence.model.User;
import persistence.repo.UserRepository;
import security.Pbkdf2PasswordHashImpl;
import security.Pbkdf2PasswordHashImpl.HashedPassword;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Stateless
@Service
public class UserService {
    @Inject private UserRepository userRepository;
    @Inject private Pbkdf2PasswordHashImpl passwordHash;

    // unique email unique username length email username password
    public int newUser(@NotBlank @Email String email,
                       @NotBlank String username,
                       @NotEmpty String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        HashedPassword hashedPassword = passwordHash.generate(password);
        user.setPassword(hashedPassword.getPassword());
        user.setSalt(hashedPassword.getSalt());
        user.setAdmin(false);

        user = userRepository.insert(user);
        return user.getId();
    }
}
