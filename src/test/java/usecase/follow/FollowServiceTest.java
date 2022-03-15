package usecase.follow;

import model.entity.Follow;
import model.entity.Section;
import model.entity.User;
import model.repository.GenericRepository;
import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={FollowService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class FollowServiceTest extends ServiceTest {

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

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -8})
    void failUnFollowNotExists(int sectionId){
        Section section = spy(Section.class);
        doReturn(null).when(section).getFollow(any());
        when(genericRepository.findById(Section.class,sectionId)).thenReturn(section);
        service.unFollow(sectionId);
        verify(genericRepository,never()).remove(any());
    }

}

