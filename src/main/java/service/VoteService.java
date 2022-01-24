package service;

import persistence.model.*;
import persistence.repo.*;
import service.auth.AuthenticationRequired;
import service.dto.CurrentUser;
import service.validation.CommentExists;
import service.validation.PostExists;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class VoteService {
    @Inject private CommentRepository commentRepo;
    @Inject private UserRepository userRepo;
    @Inject private CommentVoteRepository commentVoteRepo;
    @Inject private PostVoteRepository postVoteRepo;
    @Inject private PostRepository postRepo;
    @Inject private CurrentUser currentUser;

    @AuthenticationRequired
    public void upvoteComment(@CommentExists int id){
        voteComment(id, (short) +1);
    }

    @AuthenticationRequired
    public void downvoteComment(@CommentExists int id){
        voteComment(id, (short) -1);
    }

    private void voteComment(int id, short vote) {
        Comment comment = commentRepo.findById(id);
        User user = userRepo.getByName(currentUser.getUsername());

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        CommentVote commentVote = new CommentVote();
        commentVote.setId(commentVoteId);
        commentVote.setVote(vote);
        commentVoteRepo.merge(commentVote);
    }

    @AuthenticationRequired
    public void upvotePost(@PostExists int id){
        votePost(id, (short) +1);
    }

    @AuthenticationRequired
    public void downvotePost(@PostExists int id){
        votePost(id, (short) -1);
    }

    private void votePost(int id, short vote){
        Post post = postRepo.findById(id);
        User user = userRepo.getByName(currentUser.getUsername());

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        PostVote postVote = new PostVote();
        postVote.setId(postVoteId);
        postVote.setVote(vote);
        postVoteRepo.merge(postVote);
    }

    @AuthenticationRequired
    public void unvoteComment(@CommentExists int id){
        Comment comment = commentRepo.findById(id);
        User user = userRepo.getByName(currentUser.getUsername());

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        commentVoteRepo.remove(commentVoteRepo.findById(commentVoteId)); //funziona?
    }

    @AuthenticationRequired
    public void unvotePost(@PostExists int id){
        Post post = postRepo.findById(id);
        User user = userRepo.getByName(currentUser.getUsername());

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        postVoteRepo.remove(postVoteRepo.findById(postVoteId)); //funziona?
    }

}
