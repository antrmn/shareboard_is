package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.Follow;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.GenericRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Classes(cdi = true,
        value={FollowService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class FollowServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Mock private CurrentUser currentUser;
    @Inject private FollowService service;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    void successfulFollow(int sectionId){
        User user = new User();
        user.setId(1);
        when(currentUser.getId()).thenReturn(1);
        when(genericRepository.findById(User.class,1)).thenReturn(user);
        Section section = new Section();
        section.setId(sectionId);
        when(genericRepository.findById(Section.class,sectionId)).thenReturn(section);
        Follow follow = new Follow(user,section);
        when(genericRepository.insert(any())).thenReturn(follow);
        assertDoesNotThrow(() -> service.follow(sectionId));
        Follow follow1 = service.follow(sectionId);
        assertEquals(follow,follow1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -8})
    void failFollowWithWrongId(int sectionId){
        when(genericRepository.findById(Section.class,sectionId)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.follow(sectionId));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    void successfulUnFollow(int sectionId){
        User user = new User();
        user.setId(1);
        when(currentUser.getId()).thenReturn(1);
        when(genericRepository.findById(User.class,1)).thenReturn(user);
        Section section = mock(Section.class);
        section.setId(sectionId);
        Follow follow = new Follow(user,section);
        when(section.getFollow(any())).thenReturn(follow);
        when(genericRepository.findById(Section.class,sectionId)).thenReturn(section);
        assertDoesNotThrow(() -> service.unFollow(sectionId));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -8})
    void failUnFollowWithWrongId(int sectionId){
        when(genericRepository.findById(Section.class,sectionId)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.unFollow(sectionId));
    }

}

