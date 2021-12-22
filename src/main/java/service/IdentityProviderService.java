package service;

import service.dto.UserIdentityDTO;
import persistence.model.User;
import persistence.repo.UserRepository;

import javax.inject.Inject;
import java.util.Arrays;

public class IdentityProviderService {

    @Inject
    UserRepository userRepository;

    public UserIdentityDTO getUserIdentity(String username) {
        User user = userRepository.getByName(username);
        return new UserIdentityDTO(user.getId(), user.getUsername(), user.getAdmin());
    }

    public boolean checkCredentials(String username, String password) {
        User user = userRepository.getByName(username);
        HashedPassword pwd = HashedPassword.hash(password, user.getSalt());
        return Arrays.equals(user.getPassword(), pwd.getPassword());
    }
}
