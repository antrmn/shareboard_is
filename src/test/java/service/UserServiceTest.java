package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.ExtensionMode;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.PostRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;
import rocks.limburg.cdimock.CdiMocking;
import security.Pbkdf2PasswordHashImpl;
import service.dto.UserIdentityDTO;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;


@Classes(cdi = true,
        value={Pbkdf2PasswordHashImpl.class, UserService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class UserServiceTest extends ServiceTest {

    @Mock SectionRepository sectionRepository;
    @Mock PostRepository postRepository;
    @Mock BinaryContentRepository binaryContentRepository;
    @Mock UserRepository userRepository;
    @Inject Pbkdf2PasswordHashImpl passwordHash;
    @Inject UserService service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({"1,true", "5,false", "100,true"})
    void successfulToggleAdmin(int id, boolean currentlyAdmin) {
        User user = new User();
        user.setAdmin(currentlyAdmin);
        when(userRepository.findById(id)).thenReturn(user);
        service.toggleAdmin(id);
        assertEquals(!currentlyAdmin, user.getAdmin());
    }

    @Test
    void nullToggleAdmin(){
        User user = new User();
        user.setId(1);
        user.setAdmin(null);
        when(userRepository.findById(1)).thenReturn(user);
        assertThrows(NullPointerException.class , () -> service.toggleAdmin(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 50})
    void userNotExistsToggleAdmin(int id){
        when(userRepository.findById(id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.toggleAdmin(id));
    }

    @Test
    public void testShowUsers() {
        List<User> users = IntStream.range(1, 10).mapToObj(n -> {
            User user = new User();
            user.setUsername("user" + n);
            user.setId(n);
            user.setEmail("email" + n + "@email.it");
            user.setAdmin(false);
            return user;
        }).collect(Collectors.toList());

        List<UserIdentityDTO> usersDto =
                users.stream().map(u -> new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin()))
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(users);
        assertEquals(service.showUsers(), usersDto);
    }

    @Test
    void edit() {
    }

    @Test
    void newUser() {
    }
}