package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.PersistenceIT;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import util.Pbkdf2PasswordHashImpl;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Classes(cdi = true,
        value={UserService.class,
                BinaryContentRepository.class,
                GenericRepository.class,
                UserRepository.class, Pbkdf2PasswordHashImpl.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class UserServiceIT extends PersistenceIT {
    @Mock private CurrentUser currentUser;
    @Inject private UserService service;
    @Inject private GenericRepository repository;

    private List<User> users;

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
        });
    }

    @Test
    void successfulAddUser() throws Exception {
        doThenRollback(() -> {
            int userId = service.newUser("mail@mail.com", "username", "pass");
            service.getUser(userId);
            assertEquals(userId,service.getUser(userId).getId());
        });
    }

    @Test
    void successfulRemoveBan() throws Exception {
        doThenRollback((em) -> {
            int userId = service.newUser("mail@mail.com", "username", "pass");
            em.flush();
            em.clear();
            service.delete(userId);
            em.flush();
            em.clear();
            assertThrows(ConstraintViolationException.class,() -> service.getUser(userId));
        });
    }

}
