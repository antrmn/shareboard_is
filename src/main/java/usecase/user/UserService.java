package usecase.user;

import com.sun.istack.NotNull;
import domain.entity.User;
import domain.repository.GenericRepository;
import domain.validation.*;
import media.MediaRepository;
import media.ReadLimitExceededException;
import usecase.auth.*;
import usecase.auth.Pbkdf2PasswordHash.HashedPassword;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class UserService {
    private GenericRepository genericRepository;
    private MediaRepository bcRepo;
    private CurrentUser currentUser;
    private Pbkdf2PasswordHash passwordHash;

    protected UserService(){}

    @Inject
    protected UserService(GenericRepository genericRepository,
                          MediaRepository mediaRepository, Pbkdf2PasswordHash passwordHash,
                          CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.bcRepo = mediaRepository;
        this.passwordHash = passwordHash;
        this.currentUser = currentUser;
    }

    private UserProfile map(User user){
        return UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .creationDate(user.getCreationDate())
                .description(user.getDescription())
                .picture(user.getPicture())
                .username(user.getUsername())
                .isAdmin(user.getAdmin()).build();
    }

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
     * @param name di un utente esistente
     * @return entita UserProfile
     */
    public UserProfile getUser(@NotBlank @UserExists String name){
        User u = genericRepository.findByNaturalId(User.class,name);
        return map(u);
    }

    /**
     * Ritorna un entita UserProfile dato un id
     * @param id di un utente esistente
     * @return entita UserProfile
     */
    public UserProfile getUser(@UserExists int id){
        User u = genericRepository.findById(User.class,id);
        return map(u);
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
    public List<UserProfile> showUsers(){
        List<User> users = genericRepository.findAll(User.class);
        return users.stream().map(this::map).collect(Collectors.toList());
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
    public void edit(@Valid UserEditPage edit,
                     @UserExists int id){
        if(currentUser.getId() != id && !currentUser.isAdmin())
            throw new AuthorizationException();

        User u = genericRepository.findById(User.class,id);

        if(edit.getDescription() != null){
            u.setDescription(edit.getDescription());
        }
        if(edit.getEmail() != null){
            u.setEmail(edit.getEmail());
        }
        if(edit.getPassword() != null){
            HashedPassword hashedPassword = passwordHash.generate(edit.getPassword());
            u.setPassword(hashedPassword.getPassword());
            u.setSalt(hashedPassword.getSalt());
        }
        if(edit.getPicture() != null){
            try {
                u.setPicture(bcRepo.insert(edit.getPicture()));
            } catch(ReadLimitExceededException e){
                throw new IllegalArgumentException("Il file non deve superare i 5MB");
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
    public int newUser(@NotNull @EmailFormat @UniqueEmail String email,
                       @NotNull @UsernameFormat @UniqueUsername String username,
                       @NotNull @PasswordFormat String password){
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
