package usecase.comment;

import media.MediaRepository;
import model.entity.Comment;
import model.entity.Post;
import model.entity.User;
import model.repository.CommentRepository;
import model.repository.GenericRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Classes(cdi = true,
        value={CommentService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class CommentServiceTest extends ServiceTest {

    @Mock GenericRepository genericRepository;
    @Mock private CommentRepository commentRepo;
    @Mock private MediaRepository bcRepo;
    @Mock private CurrentUser currentUser;
    @Inject private CommentService service;


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeleteCommentAsAdmin(int id){
        Comment comment = new Comment();
        comment.setId(id);
        User user = new User();
        user.setId(id+1);
        comment.setAuthor(user);
        when(currentUser.getId()).thenReturn(88);
        when(currentUser.isAdmin()).thenReturn(true);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeleteCommentAsAuthor(int id){
        Comment comment = new Comment();
        comment.setId(id);
        User user = new User();
        user.setId(id);
        comment.setAuthor(user);
        when(currentUser.getId()).thenReturn(id);
        when(currentUser.isAdmin()).thenReturn(false);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failDeleteComment(int id){
        Comment comment = new Comment();
        comment.setId(id);
        User user = new User();
        user.setId(id+1);
        comment.setAuthor(user);
        when(currentUser.getId()).thenReturn(id);
        when(currentUser.isAdmin()).thenReturn(false);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertThrows(AuthorizationException.class, () -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDeleteCommentWithWrongId(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class, () -> service.delete(id));
    }

    @Test
    void succesfulNewComment(){
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPost(post);
        comment.setAuthor(user);
        when(genericRepository.insert(any())).thenReturn(comment);
        when(genericRepository.findById(Post.class, 1)).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        int id = service.newComment("content", 1);
        assertEquals(1, id);
    }

    @Test
    void failNewCommentWithWrongPostId(){
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPost(post);
        when(genericRepository.insert(any())).thenReturn(comment);
        assertThrows(ConstraintViolationException.class,() -> service.newComment("content", 1));
    }

    @Test
    void failNewComment(){
        Post post = new Post();
        post.setId(1);
        when(genericRepository.findById(Post.class, 1)).thenReturn(post);
        when(genericRepository.insert(any())).thenReturn(null);
        assertThrows(java.lang.NullPointerException.class,() -> service.newComment("content", 1));
    }

    @Test
    void succesfulNewCommentReply(){
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPost(post);
        comment.setAuthor(user);
        Comment parent = new Comment();
        parent.setId(2);
        parent.setPost(post);
        parent.setAuthor(user);
        when(genericRepository.insert(parent)).thenReturn(parent);
        when(genericRepository.insert(any())).thenReturn(comment);
        when(genericRepository.findById(Post.class, 1)).thenReturn(post);
        when(genericRepository.findById(User.class, 1)).thenReturn(user);
        when(genericRepository.findById(Comment.class, 2)).thenReturn(parent);
        int id = service.newCommentReply("reply", parent.getId());
        assertEquals(1, id);
    }

    @Test
    void failNewCommentWithWrongParentId(){
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPost(post);
        Comment parent = new Comment();
        parent.setId(2);
        parent.setPost(post);
        when(genericRepository.insert(any())).thenReturn(comment);
        when(genericRepository.findById(Post.class, 1)).thenReturn(post);
        assertThrows(ConstraintViolationException.class,() -> service.newCommentReply("reply", parent.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowComment(int id){
        Comment comment = spy(Comment.class);
        User author = spy(User.class);
        Post post = spy(Post.class);
        post.setId(1);
        author.setId(1);
        author.setUsername("author");
        comment.setId(id);
        comment.setContent("content");
        comment.setAuthor(author);
        comment.setPost(post);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        CommentDTO commentDTO = service.getComment(id);
        assertNotNull(commentDTO);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failShowCommentWithWrongId(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulEditCommentAsAdmin(int id) {
        when(currentUser.isAdmin()).thenReturn(true);
        User user = new User();
        user.setId(2);
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("test");
        comment.setAuthor(user);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.editComment(id, "new text"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulEditCommentAsAuthor(int id) {
        when(currentUser.getId()).thenReturn(4);
        User user = new User();
        user.setId(4);

        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("test");
        comment.setAuthor(user);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.editComment(id, "new text"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failEditCommentAsNotAuthor(int id) {
        when(currentUser.getId()).thenReturn(4);
        User user = new User();
        user.setId(21);

        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("test");
        comment.setAuthor(user);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertThrows(AuthorizationException.class,() -> service.editComment(id, "new text"));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failEditCommentWithWrongID(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.editComment(id, "net text"));
    }

}
