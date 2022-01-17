package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.UserRepository;
import security.Pbkdf2PasswordHashImpl;
import security.Pbkdf2PasswordHashImpl.HashedPassword;
import service.dto.UserEditPage;
import service.dto.UserIdentityDTO;
import service.validation.UserExists;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.annotation.security.RolesAllowed;

@Stateless
@Service
public class UserService {
    @Inject private UserRepository userRepo;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private Pbkdf2PasswordHashImpl passwordHash;

    @RolesAllowed({"admin"})
    @Transactional
    public void toggleAdmin(@UserExists int id){
        User u = userRepo.findById(id);
        u.setAdmin(!u.getAdmin());
    }

    public List<UserIdentityDTO> showUsers(){
        List<User> users = userRepo.findAll();
        List<UserIdentityDTO> usersDTO = new ArrayList<>();

        for(User u : users){
            UserIdentityDTO userDTO = new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    @RolesAllowed({"admin"})
    @Transactional
    public void delete(@UserExists int id){
        userRepo.remove(userRepo.findById(id));
    }

    public UserIdentityDTO Get(int id){
        User u = userRepo.findById(id);
        return new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
    }

    @RolesAllowed({"admin", "user"})
    @Transactional
    public void edit(UserEditPage edit,
                     @UserExists int id){
        User u = userRepo.findById(id);
        HashedPassword hashedPassword = passwordHash.generate(edit.getPassword());
        u.setPassword(hashedPassword.getPassword());
        u.setSalt(hashedPassword.getSalt());
        u.setDescription(edit.getDescription());

        if(edit.getPicture() != null){
            try {
                u.setPicture(bcRepo.insert(edit.getPicture()));
            } catch (IOException e) {
                throw new RuntimeException(e); //todo: delet this
            }
        }
    }

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

        user = userRepo.insert(user);
        return user.getId();
    }
}
