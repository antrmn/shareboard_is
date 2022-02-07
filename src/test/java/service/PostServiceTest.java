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
import persistence.repo.*;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import service.dto.PostEditDTO;
import service.dto.PostPage;
import service.dto.PostSearchForm;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    @Mock private SectionRepository sectionRepo;
    @Mock private UserRepository userRepo;
    @Mock private PostRepository postRepo;
    @Mock private CurrentUser currentUser; //Mock necessario anche se inutilizzato
    @Inject private PostService service;


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
        post.setSection(section);
        post.setContent("text");
        when(genericRepository.insert(any())).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(sectionRepo.getByName("section1")).thenReturn(section);
        int id = service.newPost("title", "text", "section1");
        assertEquals(1, id);
    }

    private static BufferedInputStream createFakeImageStream(){
        byte[] ar = {(byte)0xFF , (byte)0xD8, (byte)0xFF, (byte)0xE0};
        ByteArrayInputStream bai = new ByteArrayInputStream(ar);
        BufferedInputStream stream = new BufferedInputStream(bai);
        return stream;
    }

    @Test
    void succesfulNewImagePost() throws IOException {
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        Section section = new Section();
        section.setName("section1");
        section.setId(1);
        post.setAuthor(user);
        post.setSection(section);
        BufferedInputStream stream = createFakeImageStream();
        when(genericRepository.insert(any())).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(sectionRepo.getByName("section1")).thenReturn(section);
        when(bcRepo.insert(any())).thenReturn("pictureName");
        int id = service.newPost("title", stream, 1, "section1");
        assertEquals(1, id);
    }

    @Test
    void failNewPostWithWrongSectionName(){
        Post post = new Post();
        post.setId(1);
        Section section = new Section();
        section.setName("wrongname");
        post.setSection(section);
        when(genericRepository.insert(any())).thenReturn(post);
        assertThrows(ConstraintViolationException.class,() -> service.newPost("title", "text", "wrongname"));
    }

    @Test
    void failNewPost(){
        Section section = new Section();
        section.setId(1);
        section.setName("section");
        when(sectionRepo.getByName("section")).thenReturn(section);
        when(genericRepository.insert(any())).thenReturn(null);
        assertThrows(java.lang.NullPointerException.class,() -> service.newPost("title", "text", "section"));
    }

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

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowPost(int id){
        Post post = spy(Post.class);
        User author = spy(User.class);
        Section section = spy(Section.class);
        post.setId(1);
        post.setSection(section);
        post.setAuthor(author);
        author.setId(1);
        author.setUsername("author");
        section.setId(id);
        section.setName("section");
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        PostPage postPage = service.getPost(id);
        assertNotNull(postPage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failShowPostWithWrongId(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getPost(id));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"section1", "section2", "section3"})
//    void failShowPostWithWrongSectionName(String sName){
//        when(genericRepository.findById(Post.class,1)).thenReturn(post);
//        assertThrows(ConstraintViolationException.class,() -> service.getPost(id));
//    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulEditTextPost(int id) {
        Post post = new Post();
        post.setId(id);
        post.setContent("test");
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        PostEditDTO postEdit = new PostEditDTO("title", "content", Post.Type.TEXT);
        assertDoesNotThrow(() -> service.editPost(postEdit, id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failEditTextPostWithWrongID(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        PostEditDTO postEdit = new PostEditDTO("title", "content", Post.Type.TEXT);
        assertThrows(ConstraintViolationException.class,() -> service.editPost(postEdit, id));
    }

    @Test
    void successfulFindPosts(){
        Post post = spy(Post.class);
        Post post1 = spy(Post.class);
        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post);
        when(postRepo.getFinder().getResults()).thenReturn(posts);
        PostSearchForm postSearchForm = PostSearchForm.builder()
                .content("content")
                .onlyFollow(false)
                .includeBody(true)
                .sectionName("section")
                .authorName("author")
                .build();
        assertDoesNotThrow(() -> service.loadPosts(postSearchForm));
    }

}
