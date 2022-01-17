package service;

import http.util.InputValidator;
import persistence.model.*;
import persistence.repo.*;
import service.dto.LoggedInUser;
import service.exception.BadRequestException;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
public class VoteService {
    @Inject private CommentRepository commentRepo;
    @Inject private UserRepository userRepo;
    @Inject private CommentVoteRepository commentVoteRepo;
    @Inject private PostVoteRepository postVoteRepo;
    @Inject private PostRepository postRepo;
    @Resource private LoggedInUser loggedInUser;

    @Transactional
    @RolesAllowed({"user","admin"})
    public void VoteComment(String _id, String vote){
        if (_id == null || !InputValidator.assertInt(_id) || vote == null
                || (!vote.equalsIgnoreCase("upvote") && !vote.equalsIgnoreCase("downvote"))) {
            throw new BadRequestException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);
        Comment comment = commentRepo.findById(id);
        if(comment == null){
            throw new BadRequestException("Il commento non esiste");
        }
        User user = userRepo.getByName(loggedInUser.getUsername());
        int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        CommentVote commentVote = new CommentVote();
        commentVote.setId(commentVoteId);
        commentVote.setVote((short)_vote);

        commentVoteRepo.insert(commentVote);
    }

    @Transactional
    @RolesAllowed({"user","admin"})
    public void VotePost(String _id, String vote){
        if (_id == null || !InputValidator.assertInt(_id) || vote == null
                || (!vote.equalsIgnoreCase("upvote") && !vote.equalsIgnoreCase("downvote"))) {
            throw new BadRequestException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Post post = postRepo.findById(id);
        if(post == null){
            throw new BadRequestException("Il post non esiste");
        }
        User user = userRepo.getByName(loggedInUser.getUsername());
        int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        PostVote postVote = new PostVote();
        postVote.setId(postVoteId);
        postVote.setVote((short)_vote);

        postVoteRepo.insert(postVote);
    }

    @Transactional
    @RolesAllowed({"user","admin"})
    public void UnvoteComment(String _id){
        if (_id == null || !InputValidator.assertInt(_id)) {
            throw new BadRequestException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Comment comment = commentRepo.findById(id);
        if(comment == null){
            throw new BadRequestException("Il commento non esiste");
        }
        User user = userRepo.getByName(loggedInUser.getUsername());

        CommentVote.Id commentVoteId = new CommentVote.Id();
        commentVoteId.setComment(comment);
        commentVoteId.setUser(user);

        commentVoteRepo.remove(commentVoteRepo.findById(commentVoteId)); //funziona?
    }

    @Transactional
    @RolesAllowed({"user","admin"})
    public void UnvotePost(String _id){
        if (_id == null || !InputValidator.assertInt(_id)) {
            throw new BadRequestException("BAD REQUEST");
        }
        int id = Integer.parseInt(_id);

        Post post = postRepo.findById(id);
        if(post == null){
            throw new BadRequestException("Il post non esiste");
        }
        User user = userRepo.getByName(loggedInUser.getUsername());

        PostVote.Id postVoteId = new PostVote.Id();
        postVoteId.setPost(post);
        postVoteId.setUser(user);

        postVoteRepo.remove(postVoteRepo.findById(postVoteId)); //funziona?
    }

}
