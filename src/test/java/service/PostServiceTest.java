package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.mockito.Mock;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.PostRepository;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;

import javax.inject.Inject;


@Classes(cdi = true,
        value={PostService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class PostServiceTest extends ServiceTest{

    @Mock
    GenericRepository genericRepository;
    @Mock PostRepository postRepository;
    @Mock UserRepository userRepository;
    @Mock BinaryContentRepository binaryContentRepository;
    @Inject PostService service;

//    @Test
//    public void noSectionFoundNewPost(){
//        String sectionName = "idontexist";
//        when(genericRepository.getByName(sectionName)).thenReturn(null);
//        assertThrows(ConstraintViolationException.class,
//                () -> service.newPost("title", "body", sectionName));
//    }

//    @Test
//    void succesfulNewPost(){
//        Post post = new Post();
//        post.setId(1);
//        String sectionName = "test";
//        Section section = new Section();
//        section.setId(1);
//        section.setName(sectionName);
//        when(genericRepository.insert(any())).thenReturn(section);
//        when(genericRepository.insert(any())).thenReturn(post);
//        int id = service.newPost("title", "content", sectionName);
//        AssertNotNull(id);
//    }

}