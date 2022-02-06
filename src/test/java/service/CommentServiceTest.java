package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.Comment;
import persistence.model.Post;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.CommentRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import service.dto.CommentDTO;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={CommentService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class CommentServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Mock private CommentRepository commentRepo;
    @Mock private BinaryContentRepository bcRepo;
    @Mock private CurrentUser currentUser; //Mock necessario anche se inutilizzato
    @Inject private CommentService service;
//    @Inject private PostService postService;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeleteComment(int id){
        Comment comment = new Comment();
        comment.setId(id);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDeleteCommentWithWrongId(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class, () -> service.delete(id));
    }

//    @Test
//    void succesfulNewComment(){
//        Post post = new Post();
//        post.setId(1);
////        Comment comment = new Comment();
////        comment.setId(1);
//////        when(genericRepository.insert(post)).thenReturn(post);
////        when(genericRepository.insert(any())).thenReturn(comment);
////        CommentDTO commentDTO = new CommentDTO(1,"name",1,"content", Instant.now(),0,1, 0, 0);
//        when(genericRepository.findById(Post.class, 1)).thenReturn(post);
//        int id = service.newComment("content", 1);
//        assertNotNull(id);
//    }
//
//    @Test
//    void failNewComment(){
//        when(genericRepository.insert(any())).thenReturn(null);
//        assertThrows(NullPointerException.class,() -> service.newComment("content", 1));
//    }
//
//    @Test
//    void failNewCommentWithWrongPostId(){
//        int postId = 1;
//        Comment comment = new Comment();
//        comment.setId(1);
//        when(genericRepository.insert(any())).thenReturn(comment);
//        CommentDTO commentDTO = new CommentDTO(1,"name",1,"content", Instant.now(),0,postId, 0, 0);
//        int id = service.newComment("content", 1);
//        assertThrows(NullPointerException.class,() -> postService.getPost(postId));
//    }
//
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
    void successfulEditComment(int id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("test");
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        assertDoesNotThrow(() -> service.editComment(id, "new text"));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failEditCommentWithWrongID(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.editComment(id, "net text"));
    }

}
