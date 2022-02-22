package service;

import persistence.model.User;
import persistence.repo.GenericRepository;
import persistence.repo.UserRepository;
import service.dto.CurrentUser;
import util.Pbkdf2PasswordHashImpl;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.Instant;

@SessionScoped
@Transactional
public class AuthenticationService implements Serializable {
    private final UserRepository userRepository;
    private final GenericRepository genericRepository;
    private final Pbkdf2PasswordHashImpl passwordHash;
    private int currentUserId = 0;

    @Inject
    protected AuthenticationService(UserRepository userRepository, GenericRepository genericRepository,
                                    Pbkdf2PasswordHashImpl passwordHash){
        this.userRepository = userRepository;
        this.genericRepository = genericRepository;
        this.passwordHash = passwordHash;
    }


    /**
     * Autentica un utente
     * @param username stringa con nome utente
     * @param password stringa con password
     * @return esito dell'operazione
     */
    public boolean authenticate(String username, String password){
        User user = userRepository.getByName(username);
        if(user == null)
            return false;
        if (passwordHash.verify(password, user.getPassword(), user.getSalt())){
            currentUserId = user.getId();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ritorna l'utente in uso e ne rende accessibile i dati nelle jsp
     * @return utente in uso
     */
    @Named("currentUser") //accessibile nelle jsp con ${currentUser}
    @RequestScoped
    @Produces
    public CurrentUser getCurrentUser(){
        if(currentUserId <= 0)
            return new CurrentUser();

        User user = genericRepository.findById(User.class,currentUserId);
        if(user == null)
            return new CurrentUser();

        Instant longestCurrentBan = user.getBans().isEmpty() ? null : user.getBans().get(0).getEndTime();

        CurrentUser currentUser = CurrentUser.builder()
                .username(user.getUsername())
                .id(user.getId())
                .isAdmin(user.getAdmin())
                .picture(user.getPicture())
                .banDuration(longestCurrentBan)
                .isLoggedIn(true)
                .build();

        System.out.println(currentUser);
        return currentUser;
    }

}
