package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.*;
import persistence.repo.*;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Classes(cdi = true,
        value={VoteService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class VoteServiceTest extends ServiceTest{

    @Mock private CommentRepository commentRepo;
    @Mock private UserRepository userRepo;
    @Mock private CommentVoteRepository commentVoteRepo;
    @Mock private PostVoteRepository postVoteRepo;
    @Mock private PostRepository postRepo;
    @Mock private CurrentUser currentUser;
    @Inject private VoteService service;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUpvoteComment(int id){
        Comment comment = new Comment();
        when(commentRepo.findById(id)).thenReturn(comment);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.merge(any())).thenReturn(commentVote);
        assertDoesNotThrow(() -> service.upvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUpvoteCommentWithWrongId(int id){
        when(commentRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.merge(any())).thenReturn(commentVote);
        assertThrows(ConstraintViolationException.class,() -> service.upvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDownvoteComment(int id){
        Comment comment = new Comment();
        when(commentRepo.findById(id)).thenReturn(comment);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.merge(any())).thenReturn(commentVote);
        assertDoesNotThrow(() -> service.downvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDownvoteCommentWithWrongId(int id){
        when(commentRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.merge(any())).thenReturn(commentVote);
        assertThrows(ConstraintViolationException.class,() -> service.downvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUpvotePost(int id){
        Post post = new Post();
        when(postRepo.findById(id)).thenReturn(post);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.merge(any())).thenReturn(postVote);
        assertDoesNotThrow(() -> service.upvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUpvotePostWithWrongId(int id){
        when(postRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.merge(any())).thenReturn(postVote);
        assertThrows(ConstraintViolationException.class,() -> service.upvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDownvotePost(int id){
        Post post = new Post();
        when(postRepo.findById(id)).thenReturn(post);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.merge(any())).thenReturn(postVote);
        assertDoesNotThrow(() -> service.downvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDownvotePostWithWrongId(int id){
        when(postRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.merge(any())).thenReturn(postVote);
        assertThrows(ConstraintViolationException.class,() -> service.downvotePost(id));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUnvoteComment(int id){
        Comment comment = new Comment();
        when(commentRepo.findById(id)).thenReturn(comment);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.findById(any())).thenReturn(commentVote);
        assertDoesNotThrow(() -> service.unvoteComment(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUnvoteComment(int id){
        when(commentRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        CommentVote commentVote = new CommentVote();
        when(commentVoteRepo.findById(any())).thenReturn(commentVote);
        assertThrows(ConstraintViolationException.class,() -> service.unvoteComment(id));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulUnvotePost(int id){
        Post post = new Post();
        when(postRepo.findById(id)).thenReturn(post);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.findById(any())).thenReturn(postVote);
        assertDoesNotThrow(() -> service.unvotePost(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failUnvotePost(int id){
        when(postRepo.findById(id)).thenReturn(null);
        User user = new User();
        when(currentUser.getUsername()).thenReturn("username");
        when(userRepo.getByName(any())).thenReturn(user);
        PostVote postVote = new PostVote();
        when(postVoteRepo.findById(any())).thenReturn(postVote);
        assertThrows(ConstraintViolationException.class,() -> service.unvotePost(id));
    }

}
