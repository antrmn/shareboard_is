package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.GenericRepository;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;
import util.Pbkdf2PasswordHashImpl;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Classes(cdi=true, value = {AuthenticationService.class, Pbkdf2PasswordHashImpl.class},
            cdiInterceptors = BValInterceptor.class,
            cdiStereotypes = CdiMock.class)
class AuthenticationServiceTest extends ServiceTest {
    @Mock UserRepository userRepo;
    @Mock GenericRepository genericRepo;
    @Inject Pbkdf2PasswordHashImpl passwordHash;
    @Inject AuthenticationService authenticationService;

    @Test
    void authenticateUserDoesntExist(){
        when(userRepo.getByName(any())).thenReturn(null);
        assertFalse(authenticationService.authenticate("user", "pass"));
    }

    @Test
    void authenticationPasswordMatches(){
        User user = new User();
        user.setUsername("user");
        user.setId(5);
        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());

        when(userRepo.getByName(any())).thenReturn(user);
        assertTrue(authenticationService.authenticate("user", "password"));
    }

    @Test
    void authenticationPasswordDoesntMatch(){
        User user = new User();
        user.setUsername("user");



        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());

        when(userRepo.getByName(any())).thenReturn(user);
        assertFalse(authenticationService.authenticate("user", "notpassword"));
    }

    @Test
    void getCurrentUserIdZero(){
        User user = new User();
        user.setUsername("user");
        user.setId(0);
        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());
        when(userRepo.getByName(any())).thenReturn(user);
        assertTrue(authenticationService.authenticate("user", "password"));

        assertFalse(authenticationService.getCurrentUser().isLoggedIn());
    }

    @Test
    void getCurrentUserNotFound(){
        User user = new User();
        user.setUsername("user");
        user.setId(2);
        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());
        when(userRepo.getByName(any())).thenReturn(user);
        assertTrue(authenticationService.authenticate("user", "password"));


        when(genericRepo.findById(User.class,2)).thenReturn(null);
        assertFalse(authenticationService.getCurrentUser().isLoggedIn());
    }

    @Test
    void getCurrentUserWithoutBan(){
        User user = new User();
        user.setUsername("user");
        user.setId(2);
        user.setAdmin(false);
        user = spy(user);
        when(user.getBans()).thenReturn(List.of());

        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());
        when(userRepo.getByName(any())).thenReturn(user);
        assertTrue(authenticationService.authenticate("user", "password"));


        when(genericRepo.findById(User.class,2)).thenReturn(user);
        assertNull(authenticationService.getCurrentUser().getBanDuration());
    }

    @Test
    void getCurrentUserWithBan(){
        User user = new User();
        user.setUsername("user");
        user.setId(2);
        user.setAdmin(false);
        user = spy(user);
        Ban ban = new Ban();
        ban.setEndTime(Instant.MAX);
        when(user.getBans()).thenReturn(List.of(ban));

        Pbkdf2PasswordHashImpl.HashedPassword expected = passwordHash.generate("password");
        user.setPassword(expected.getPassword());
        user.setSalt(expected.getSalt());
        when(userRepo.getByName(any())).thenReturn(user);
        assertTrue(authenticationService.authenticate("user", "password"));


        when(genericRepo.findById(User.class,2)).thenReturn(user);
        assertEquals(authenticationService.getCurrentUser().getBanDuration(), ban.getEndTime());
    }
}