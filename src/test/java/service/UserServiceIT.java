package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.*;
import service.dto.BanDTO;
import util.Pbkdf2PasswordHashImpl;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Classes(cdi = true,
        value={UserService.class,
                BinaryContentRepository.class,
                GenericRepository.class,
                UserRepository.class, Pbkdf2PasswordHashImpl.class},
        cdiInterceptors = BValInterceptor.class)
public class UserServiceIT extends PersistenceIT {
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
