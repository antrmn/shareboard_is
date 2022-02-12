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

    /**
     * Inverte lo stato di admin di un utente dato un id
     * @param id id di un utente esistente
     */
    @AdminsOnly
    public void toggleAdmin(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        u.setAdmin(!u.getAdmin());
    }

    /**
     * Ritorna un entita UserProfile dato un nome
     * @param nome di un utente esistente
     * @return entita UserProfile
     */
    public UserProfile getUser(@UserExistsByName String name){
        User u = userRepo.getByName(name);
        UserProfile user = new UserProfile(u.getId(), u.getUsername(), u.getEmail(), u.getCreationDate(), u.getPicture(), u.getDescription());
        return user;
    }

    /**
     * Ritorna un entita UserProfile dato un id
     * @param id di un utente esistente
     * @return entita UserProfile
     */
    public UserProfile getUser(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        UserProfile user = new UserProfile(u.getId(), u.getUsername(), u.getEmail(), u.getCreationDate(), u.getPicture(), u.getDescription());
        return user;
    }

    /**
     * Ritorna un entita UserIdentityDTO dato un id
     * @param id di un utente esistente
     * @return entita UserIdentityDTO
     */
    public UserIdentityDTO get(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        return new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
    }

    /**
     * Ritorna un lo username di un utente dato un id
     * @param id di un utente esistente
     * @return nome dell'utente
     */
    public String getUsernameById(@UserExists int id){
        return genericRepository.findById(User.class,id).getUsername();
    }

    /**
     * Ritorna un lista di tutte le gli utenti
     * @return lista di entita UserIdentityDTO
     */
    public List<UserIdentityDTO> showUsers(){
        List<User> users = genericRepository.findAll(User.class);
        List<UserIdentityDTO> usersDTO = new ArrayList<>();

        for(User u : users){
            UserIdentityDTO userDTO = new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    /**
     * Elimina un utente dato un id
     * @param id di un utente esistente
     */
    @AdminsOnly
    public void delete(@UserExists int id){
        genericRepository.remove(genericRepository.findById(User.class, id));
    }

    /**
     * Modifica i dati di un utente dato un id
     * @param edit nuovi dati dell'utente
     * @param id di un utente esistente
     */
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

    /**
     * Crea un nuovo utente e ne restituisce l'id
     * @param email stringa non vuota, unica e in formato email
     * @param username stringa non vuota e unica
     * @param password Stringa non vuota con minimo: 3 e massimo: 255 caratteri
     * @return identificativo dell'utente creato
     */
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
