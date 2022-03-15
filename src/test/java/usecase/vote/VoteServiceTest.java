package usecase.vote;

import model.entity.*;
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
        value={VoteService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class VoteServiceTest extends ServiceTest {

    @Mock GenericRepository genericRepository;
    @Mock private CurrentUser currentUser;
    @Inject private VoteService service;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUpvoteComment(int id){
        Comment comment = new Comment();
        comment.setId(id);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        CommentVote commentVote = new CommentVote(user,comment,(short)1);
        when(genericRepository.merge(any())).thenReturn(commentVote);
        assertDoesNotThrow(() -> service.upvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUpvoteCommentWithWrongId(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.upvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDownvoteComment(int id){
        Comment comment = new Comment();
        comment.setId(id);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        CommentVote commentVote = new CommentVote(user,comment,(short)-1);
        when(genericRepository.merge(any())).thenReturn(commentVote);
        assertDoesNotThrow(() -> service.downvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDownvoteCommentWithWrongId(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.downvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUpvotePost(int id){
        Post post = new Post();
        post.setId(id);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        PostVote postVote = new PostVote(user,post,(short)1);
        when(genericRepository.merge(any())).thenReturn(postVote);
        assertDoesNotThrow(() -> service.upvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUpvotePostWithWrongId(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.upvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDownvotePost(int id){
        Post post = new Post();
        post.setId(id);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        PostVote postVote = new PostVote(user,post,(short)-1);
        when(genericRepository.merge(any())).thenReturn(postVote);
        assertDoesNotThrow(() -> service.downvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDownvotePostWithWrongId(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.downvotePost(id));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUnvoteComment(int id){
        Comment comment = spy(Comment.class);
        comment.setId(id);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        CommentVote commentVote = new CommentVote(user,comment,(short)1); //1?
        doReturn(commentVote).when(comment).getVote(any());
        assertDoesNotThrow(() -> service.unvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUnvoteComment(int id){
        when(genericRepository.findById(Comment.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.unvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failUnvoteCommentVoteNotExists(int id){
        Comment comment = spy(Comment.class);
        comment.setId(id);
        when(genericRepository.findById(Comment.class,id)).thenReturn(comment);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        doReturn(null).when(comment).getVote(any());
        service.unvoteComment(id);
        verify(genericRepository,never()).remove(any());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUnvotePost(int id){
        Post post = spy(Post.class);
        post.setId(id);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        User user = new User();
        user.setId(1);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        PostVote postVote = new PostVote(user,post,(short)1);
        doReturn(postVote).when(post).getVote(any());
        assertDoesNotThrow(() -> service.unvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUnvotePost(int id){
        when(genericRepository.findById(Post.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.unvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failUnvotePostVoteNotExists(int id){
        Post post = spy(Post.class);
        post.setId(id);
        when(genericRepository.findById(Post.class,id)).thenReturn(post);
        User user = new User();
        user.setId(2);
        when(currentUser.getId()).thenReturn(2);
        when(genericRepository.findById(User.class,2)).thenReturn(user);
        doReturn(null).when(post).getVote(any());
        service.upvotePost(id);
        verify(genericRepository,never()).remove(any());
    }

}
