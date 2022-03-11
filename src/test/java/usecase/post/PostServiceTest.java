package usecase.post;

import media.MediaRepository;
import model.entity.Post;
import model.entity.Section;
import model.entity.User;
import model.repository.GenericRepository;
import model.repository.PostRepository;
import model.repository.SectionRepository;
import model.repository.UserRepository;
import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;
import usecase.auth.AuthorizationException;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={PostService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class PostServiceTest extends ServiceTest {

    @Mock GenericRepository genericRepository;
    @Mock private MediaRepository bcRepo;
    @Mock private SectionRepository sectionRepo;
    @Mock private UserRepository userRepo;
    @Mock private PostRepository postRepo;
    @Mock private PostRepository.PostFinder postFinder;
    @Mock private CurrentUser currentUser; //Mock necessario anche se inutilizzato
    @Inject private PostService service;

    private static BufferedInputStream createFakeImageStream(){
        byte[] ar = {(byte)0xFF , (byte)0xD8, (byte)0xFF, (byte)0xE0};
        ByteArrayInputStream bai = new ByteArrayInputStream(ar);
        BufferedInputStream stream = new BufferedInputStream(bai);
        return stream;
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
        post.setSection(section);
        post.setContent("text");
        when(genericRepository.insert(any())).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(genericRepository.findByNaturalId(Section.class,"section1")).thenReturn(section);
        int id = service.newPost("title", "text", "section1");
        assertEquals(1, id);
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
        when(bcRepo.insert(any())).thenReturn("pictureName");
        when(genericRepository.insert(any())).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(genericRepository.findByNaturalId(Section.class,"section1")).thenReturn(section);
        int id = service.newPost("title", stream, "section1");
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
    void failNewTextPost(){
        Section section = new Section();
        section.setId(1);
        section.setName("usecase/section");
        when(genericRepository.findByNaturalId(Section.class, "usecase/section")).thenReturn(section);
        when(genericRepository.insert(any())).thenReturn(null);
        assertThrows(java.lang.NullPointerException.class,() -> service.newPost("title", "text", "usecase/section"));
    }

    @Test
    void failNewImagePost() throws IOException {
        Section section = new Section();
        section.setId(1);
        section.setName("usecase/section");
        BufferedInputStream stream = createFakeImageStream();
        when(bcRepo.insert(any())).thenReturn("pictureName");
        when(genericRepository.findByNaturalId(Section.class, "usecase/section")).thenReturn(section);
        when(genericRepository.insert(any())).thenReturn(null);
        assertThrows(java.lang.NullPointerException.class,() -> service.newPost("title", stream, "usecase/section"));
    }

    @Test
    void failNewImagePostWithWrongStreamType() throws IOException {
        Post post = new Post();
        post.setId(1);
        Section section = new Section();
        section.setId(1);
        section.setName("usecase/section");
        BufferedInputStream stream = new BufferedInputStream(InputStream.nullInputStream());
        when(bcRepo.insert(any())).thenReturn("pictureName");
        when(genericRepository.findByNaturalId(Section.class, "usecase/section")).thenReturn(section);
        when(genericRepository.insert(any())).thenReturn(post);
        assertThrows(ConstraintViolationException.class,() -> service.newPost("title", stream, "usecase/section"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeletePostAsAdmin(int id){
        Post post = new Post();
        post.setId(id);
        User user = new User();
        user.setId(id+1);
        post.setAuthor(user);
        when(currentUser.getId()).thenReturn(5);
        when(currentUser.isAdmin()).thenReturn(true);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeletePostAsAuthor(int id){
        Post post = new Post();
        post.setId(id);
        User user = new User();
        user.setId(id);
        post.setAuthor(user);
        when(currentUser.getId()).thenReturn(id);
        when(currentUser.isAdmin()).thenReturn(false);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @Test
    void failDeletePost(){
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        post.setAuthor(user);
        when(currentUser.getId()).thenReturn(2);
        when(currentUser.isAdmin()).thenReturn(false);
        when(genericRepository.findById(Post.class,1)).thenReturn(post);
        assertThrows(AuthorizationException.class, () -> service.delete(1));
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
        section.setName("usecase/section");
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

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void successfulFindPosts(int id){

        PostSearchForm.SortCriteria criteria = PostSearchForm.SortCriteria.NEWEST;
        if (id == 1) criteria = PostSearchForm.SortCriteria.OLDEST;
        if (id == 2) criteria = PostSearchForm.SortCriteria.MOSTVOTED;

        Post post = spy(Post.class);
        User author = spy(User.class);
        Section section = spy(Section.class);
        post.setId(1);
        post.setSection(section);
        post.setAuthor(author);
        author.setId(1);
        author.setUsername("author");
        section.setId(1);
        section.setName("usecase/section");
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        when(postRepo.getFinder()).thenReturn(postFinder);
        when(postRepo.getFinder().getResults()).thenReturn(posts);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getId()).thenReturn(1);
        doReturn(new Section()).when(genericRepository).findByNaturalId(eq(Section.class),any());
        doReturn(new User()).when(genericRepository).findByNaturalId(eq(User.class),any());
        PostSearchForm postSearchForm = PostSearchForm.builder()
                .content("content")
                .onlyFollow(true)
                .includeBody(true)
                .sectionName("usecase/section")
                .authorName("author")
                .postedAfter(Instant.now())
                .postedBefore(Instant.now())
                .page(1)
                .orderBy(criteria)
                .build();
        assertEquals(posts.get(0).getId(), service.loadPosts(postSearchForm).get(0).getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void failFindPosts(int id){

        PostSearchForm.SortCriteria criteria = PostSearchForm.SortCriteria.NEWEST;
        if (id == 1) criteria = PostSearchForm.SortCriteria.OLDEST;
        if (id == 2) criteria = PostSearchForm.SortCriteria.MOSTVOTED;

        when(postRepo.getFinder()).thenReturn(postFinder);
        when(postRepo.getFinder().getResults()).thenReturn(null);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getId()).thenReturn(1);
        doReturn(new Section()).when(genericRepository).findByNaturalId(eq(Section.class),any());
        doReturn(new User()).when(genericRepository).findByNaturalId(eq(User.class),any());
        PostSearchForm postSearchForm = PostSearchForm.builder()
                .content("content")
                .onlyFollow(true)
                .includeBody(true)
                .sectionName("usecase/section")
                .authorName("author")
                .postedAfter(Instant.now())
                .postedBefore(Instant.now())
                .page(1)
                .orderBy(criteria)
                .build();

        assertThrows(java.lang.NullPointerException.class, () -> service.loadPosts(postSearchForm).get(0));
    }

}
