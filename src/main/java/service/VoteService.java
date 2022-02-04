package service;

import persistence.model.*;
import persistence.repo.GenericRepository;
import service.auth.AuthenticationRequired;
import service.dto.CurrentUser;
import service.validation.CommentExists;
import service.validation.PostExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class VoteService {
    @Inject private GenericRepository genericRepository;
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
        Comment comment = genericRepository.findById(Comment.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        CommentVote commentVote = new CommentVote(user,comment, vote);
        genericRepository.merge(commentVote);
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
        Post post = genericRepository.findById(Post.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        PostVote postVote = new PostVote(user,post,vote);
        genericRepository.merge(postVote);
    }

    @AuthenticationRequired
    public void unvoteComment(@CommentExists int id){
        Comment comment = genericRepository.findById(Comment.class, id);
        User user = genericRepository.findById(User.class, currentUser.getId());
        CommentVote commentVote = comment.getVote(user);
        if(commentVote != null)
            genericRepository.remove(commentVote);
    }

    @AuthenticationRequired
    public void unvotePost(@PostExists int id){
        Post post = genericRepository.findById(Post.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        PostVote vote = post.getVote(user);
        if(vote != null)
            genericRepository.remove(vote);
    }

}
