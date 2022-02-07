package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.Post;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Classes(cdi = true,
        value={PostService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class PostServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Mock private BinaryContentRepository bcRepo;
    @Mock private CurrentUser currentUser; //Mock necessario anche se inutilizzato
    @Inject private PostService service;


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeletePost(int id){
        Post post = new Post();
        post.setId(id);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDeletePostWithWrongId(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class, () -> service.delete(id));
    }

    @Test
    void succesfulNewTextPost(){
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        Section section = new Section();
        section.setName("section1");
        section.setId(1);
        post.setAuthor(user);
        when(genericRepository.insert(any())).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(genericRepository.findById(Section.class, 1)).thenReturn(section);
        int id = service.newPost("title", "text", "section1");
        assertEquals(1, id);
    }

}
