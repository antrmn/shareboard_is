package service;

import http.util.InputValidator;
import persistence.model.*;
import persistence.repo.*;
import service.auth.AuthenticationRequired;
import service.dto.CurrentUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class VoteService {
    @Inject private CommentRepository commentRepo;
    @Inject private UserRepository userRepo;
    @Inject private CommentVoteRepository commentVoteRepo;
    @Inject private PostVoteRepository postVoteRepo;
    @Inject private PostRepository postRepo;
    @Inject private CurrentUser currentUser;

    @AuthenticationRequired
    public void VoteComment(String _id, String vote){
        if (_id == null || !InputValidator.assertInt(_id) || vote == null
                || (!vote.equalsIgnoreCase("upvote") && !vote.equalsIgnoreCase("downvote"))) {
            throw new IllegalArgumentException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);
        Comment comment = commentRepo.findById(id);
        if(comment == null){
            throw new IllegalArgumentException("Il commento non esiste");
        }
        User user = userRepo.getByName(currentUser.getUsername());
        int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        CommentVote commentVote = new CommentVote();
        commentVote.setId(commentVoteId);
        commentVote.setVote((short)_vote);

        commentVoteRepo.insert(commentVote);
    }

    @AuthenticationRequired
    public void VotePost(String _id, String vote){
        if (_id == null || !InputValidator.assertInt(_id) || vote == null
                || (!vote.equalsIgnoreCase("upvote") && !vote.equalsIgnoreCase("downvote"))) {
            throw new IllegalArgumentException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Post post = postRepo.findById(id);
        if(post == null){
            throw new IllegalArgumentException("Il post non esiste");
        }
        User user = userRepo.getByName(currentUser.getUsername());
        int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        PostVote postVote = new PostVote();
        postVote.setId(postVoteId);
        postVote.setVote((short)_vote);

        postVoteRepo.insert(postVote);
    }

    @AuthenticationRequired
    public void UnvoteComment(String _id){
        if (_id == null || !InputValidator.assertInt(_id)) {
            throw new IllegalArgumentException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Comment comment = commentRepo.findById(id);
        if(comment == null){
            throw new IllegalArgumentException("Il commento non esiste");
        }
        User user = userRepo.getByName(currentUser.getUsername());

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        commentVoteRepo.remove(commentVoteRepo.findById(commentVoteId)); //funziona?
    }

    @AuthenticationRequired
    public void UnvotePost(String _id){
        if (_id == null || !InputValidator.assertInt(_id)) {
            throw new IllegalArgumentException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Post post = postRepo.findById(id);
        if(post == null){
            throw new IllegalArgumentException("Il post non esiste");
        }
        User user = userRepo.getByName(currentUser.getUsername());

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        postVoteRepo.remove(postVoteRepo.findById(postVoteId)); //funziona?
    }

}
