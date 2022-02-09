package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.UserEditPage;
import service.dto.UserIdentityDTO;
import util.Pbkdf2PasswordHashImpl;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@Classes(cdi = true,
        value={Pbkdf2PasswordHashImpl.class, UserService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class UserServiceTest extends ServiceTest {

    @Mock BinaryContentRepository binaryContentRepository;
    @Mock UserRepository userRepository;
    @Mock GenericRepository genericRepository;
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
        user.setId(1);
        user.setAdmin(currentlyAdmin);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        service.toggleAdmin(id);
        assertEquals(!currentlyAdmin, user.getAdmin());
    }

    @Test
    void nullToggleAdmin(){
        User user = new User();
        user.setId(1);
        user.setAdmin(null);
        when(genericRepository.findById(User.class,1)).thenReturn(user);
        assertThrows(NullPointerException.class , () -> service.toggleAdmin(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 50})
    void userNotExistsToggleAdmin(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.toggleAdmin(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void successfulGetUserById(int id){
        User user = new User();
        user.setId(1);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        assertDoesNotThrow(() -> service.getUser(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, -40})
    void failGetUserWithWrongId(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getUser(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"name1", "name2", "name3"})
    void successfulGetUserByName(String name){
        User user = new User();
        user.setId(1);
        when(userRepository.getByName(name)).thenReturn(user);
        assertDoesNotThrow(() -> service.getUser(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"wrong1", "wrong2", "wrong3"})
    void failGetUserWithWrongName(String name){
        when(userRepository.getByName(name)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getUser(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void successfulGetDTOById(int id){
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setAdmin(false);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        assertDoesNotThrow(() -> service.get(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, -40})
    void failGetDTOWithWrongId(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.get(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void successfulGetUsernameById(int id){
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        assertDoesNotThrow(() -> service.getUsernameById(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void failGetUsernameWithWrongId(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getUsernameById(id));
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

        when(genericRepository.findAll(User.class)).thenReturn(users);
        assertEquals(service.showUsers(), usersDto);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void successfulDeleteById(int id){
        User user = new User();
        user.setId(1);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void failDeleteWithWrongId(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 40})
    void successfulEdit(int id) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(InputStream.nullInputStream());
        UserEditPage userEditPage = new UserEditPage(1,"description","email",stream,"password");
        User user = new User();
        user.setId(1);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        when(binaryContentRepository.insert(any())).thenReturn("pictureName");
        assertDoesNotThrow(() -> service.edit(userEditPage,id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, -40})
    void failEditWithWrongID(int id) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(InputStream.nullInputStream());
        UserEditPage userEditPage = new UserEditPage(1,"description","email",stream,"password");
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        when(binaryContentRepository.insert(any())).thenReturn("pictureName");
        assertThrows(ConstraintViolationException.class,() -> service.edit(userEditPage,id));
    }


    @ParameterizedTest
    @CsvSource({"email2@gmail.com,username,password", "email45@gmail.com,username2,password2"})
    void successfulNewUser(String email, String username, String password) {
        User user = new User();
        user.setId(1);
        when(genericRepository.insert(any())).thenReturn(user);
        assertDoesNotThrow(() -> service.newUser(email,username,password));
    }

    @ParameterizedTest
    @CsvSource({"email2gmail,username,password", "email45gmail,username2,password2"})
    void failNewUserWrongEmail(String email, String username, String password) {
        User user = new User();
        user.setId(1);
        when(genericRepository.insert(any())).thenReturn(user);
        assertThrows(ConstraintViolationException.class,() -> service.newUser(email,username,password));
    }

    @Test
    void failNewUserBlankEmail() {
        User user = new User();
        user.setId(1);
        when(genericRepository.insert(any())).thenReturn(user);
        when(userRepository.getByEmail(any())).thenReturn(user);
        assertThrows(ConstraintViolationException.class, () -> {
            service.newUser("","username","mypassword123");
        });
    }

    @Test
    void failNewUserBlankUsername() {
        User user = new User();
        user.setId(1);
        when(userRepository.getByName(any())).thenReturn(user);
        when(genericRepository.insert(any())).thenReturn(user);
        assertThrows(ConstraintViolationException.class, () -> {
            service.newUser("email@email.email"," \n\t","mypassword123");
        });
    }

    @Test
    void failNewUserEmptyPassword() {
        User user = new User();
        user.setId(1);
        when(genericRepository.insert(any())).thenReturn(user);
        assertThrows(ConstraintViolationException.class,() -> {
            service.newUser("email@email.email","username","\t");
        });
    }
}