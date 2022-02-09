package service;

import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.UserRepository;
import service.auth.AdminsOnly;
import service.auth.AuthenticationRequired;
import service.dto.UserEditPage;
import service.dto.UserIdentityDTO;
import service.dto.UserProfile;
import service.validation.*;
import util.Pbkdf2PasswordHashImpl;
import util.Pbkdf2PasswordHashImpl.HashedPassword;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserService {
    @Inject private UserRepository userRepo;
    @Inject private GenericRepository genericRepository;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private Pbkdf2PasswordHashImpl passwordHash;

    @AdminsOnly
    public void toggleAdmin(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        u.setAdmin(!u.getAdmin());
    }

    public UserProfile getUser(@UserExistsByName String name){
        User u = userRepo.getByName(name);
        UserProfile user = new UserProfile(u.getId(), u.getUsername(), u.getEmail(), u.getCreationDate(), u.getPicture(), u.getDescription());
        return user;
    }

    public UserProfile getUser(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        UserProfile user = new UserProfile(u.getId(), u.getUsername(), u.getEmail(), u.getCreationDate(), u.getPicture(), u.getDescription());
        return user;
    }


    public UserIdentityDTO get(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        return new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
    }

    public String getUsernameById(@UserExists int id){
        return genericRepository.findById(User.class,id).getUsername();
    }

    public List<UserIdentityDTO> showUsers(){
        List<User> users = genericRepository.findAll(User.class);
        List<UserIdentityDTO> usersDTO = new ArrayList<>();

        for(User u : users){
            UserIdentityDTO userDTO = new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    @AdminsOnly
    public void delete(@UserExists int id){
        genericRepository.remove(genericRepository.findById(User.class, id));
    }

    @AuthenticationRequired
    public void edit(UserEditPage edit,
                     @UserExists int id){
        User u = genericRepository.findById(User.class,id);

        if(edit.getDescription() != null){
            u.setDescription(edit.getDescription());
        }
        if(edit.getEmail() != null){
            u.setEmail(edit.getEmail());
        }




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
    public int newUser(@NotBlank @EmailFormat @UniqueEmail String email,
                       @NotBlank @UsernameFormat @UniqueUsername String username,
                       @NotEmpty @Size(min = 3,max = 255) String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        HashedPassword hashedPassword = passwordHash.generate(password);
        user.setPassword(hashedPassword.getPassword());
        user.setSalt(hashedPassword.getSalt());
        user.setAdmin(false);

        user = genericRepository.insert(user);
        return user.getId();
    }
}
