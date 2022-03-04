package usecase.auth;

import domain.entity.Post;
import domain.entity.Section;
import domain.entity.User;
import domain.repository.*;
import media.MediaRepository;
import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;
import usecase.post.PostService;
import usecase.user.UserService;

import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@Classes(cdi=true, value = {PostService.class, UserService.class},
        cdiInterceptors = {BValInterceptor.class, AuthenticationRequiredInterceptor.class,
        DenyBannedUsersInterceptor.class, AdminsOnlyInterceptor.class},
        cdiStereotypes = {CdiMock.class})
class AuthorizationInterceptorTest extends ServiceTest {

    @Inject PostService postService;
    @Inject UserService userService;
    @Mock UserRepository userRepository;
    @Mock GenericRepository genericRepository;
    @Mock PostRepository postRepo;
    @Mock CommentRepository commentRepo;
    @Mock SectionRepository sectionRepository;
    @Mock
    MediaRepository bcRepo;
    @Mock
    Pbkdf2PasswordHash passwordHash;
    @Mock CurrentUser currentUser;

    @Test
    void denyNotAdminUser(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getUsername()).thenReturn("username");
        when(currentUser.isAdmin()).thenReturn(false);
        Assertions.assertThrows(AuthorizationException.class, () -> userService.toggleAdmin(4));
    }

    @Test
    void denyGuestToggleAdmin(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isLoggedIn()).thenReturn(false);
        when(currentUser.getUsername()).thenReturn("username");
        when(currentUser.isAdmin()).thenReturn(false);
        Assertions.assertThrows(AuthorizationException.class, () -> userService.toggleAdmin(4));
    }

    @Test
    void allowAdminUser(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getUsername()).thenReturn("username");
        when(currentUser.isAdmin()).thenReturn(true);

        User user = new User();
        user.setId(4);
        user.setAdmin(true);
        when(genericRepository.findById(User.class,4)).thenReturn(user);

        assertDoesNotThrow(() -> userService.toggleAdmin(4));
    }

    @Test
    void guestAttemptNewPost(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isLoggedIn()).thenReturn(false);

        Assertions.assertThrows(AuthenticationRequiredException.class, () -> postService.newPost("a","b","c"));
    }

    @Test
    void bannedUserAttemptNewPost(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getBanDuration()).thenReturn(Instant.now().plus(1, ChronoUnit.DAYS));

        Assertions.assertThrows(BannedUserException.class, () -> postService.newPost("a","b","c"));
    }

    @Test
    void loggedInUserAttemptNewPost(){
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.getUsername()).thenReturn("username");
        when(currentUser.isLoggedIn()).thenReturn(true);

        User user = new User();
        user.setId(5);
        Section section = new Section();
        Post post = new Post();
        post.setId(5);
        doReturn(section).when(genericRepository).findByNaturalId(eq(Section.class),any());
        doReturn(new User()).when(genericRepository).findByNaturalId(eq(User.class),any());
        when(genericRepository.insert((any(Post.class)))).thenReturn(post);

        assertDoesNotThrow(() -> postService.newPost("a","b","c"));
    }



}