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
    private final GenericRepository genericRepository;
    private final CurrentUser currentUser;

    @Inject
    protected VoteService(GenericRepository genericRepository, CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.currentUser = currentUser;
    }

    /**
     * Aggiunge un voto positivo ad un commento
     * @param id di un commento esistente
     */
    @AuthenticationRequired
    public void upvoteComment(@CommentExists int id){
        voteComment(id, (short) +1);
    }

    /**
     * Aggiunge un voto negativo ad un commento
     * @param id di un commento esistente
     */
    @AuthenticationRequired
    public void downvoteComment(@CommentExists int id){
        voteComment(id, (short) -1);
    }

    /**
     * Aggiunge un voto positivo ad un post
     * @param id di un post esistente
     */
    @AuthenticationRequired
    public void upvotePost(@PostExists int id){
        votePost(id, (short) +1);
    }

    /**
     * Aggiunge un voto negativo ad un post
     * @param id di un post esistente
     */
    @AuthenticationRequired
    public void downvotePost(@PostExists int id){
        votePost(id, (short) -1);
    }

    /**
     * Rimuove il voto ad un commento
     * @param id di un commento esistente
     */
    @AuthenticationRequired
    public void unvoteComment(@CommentExists int id){
        Comment comment = genericRepository.findById(Comment.class, id);
        User user = genericRepository.findById(User.class, currentUser.getId());
        CommentVote commentVote = comment.getVote(user);
        if(commentVote != null)
            genericRepository.remove(commentVote);
    }

    /**
     * Rimuove il voto ad un post
     * @param id di un post esistente
     */
    @AuthenticationRequired
    public void unvotePost(@PostExists int id){
        Post post = genericRepository.findById(Post.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        PostVote vote = post.getVote(user);
        if(vote != null)
            genericRepository.remove(vote);
    }

    /**
     * Aggiunge un voto ad un post
     * @param id di un post
     * @param vote tipo di voto: 1 indica voto positivo, -1 voto negativo
     */
    private void votePost(int id, short vote){
        Post post = genericRepository.findById(Post.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        PostVote postVote = new PostVote(user,post,vote);
        genericRepository.merge(postVote);
    }

    /**
     * Aggiunge un voto ad un commento
     * @param id di un commento
     * @param vote tipo di voto: 1 indica voto positivo, -1 voto negativo
     */
    private void voteComment(int id, short vote) {
        Comment comment = genericRepository.findById(Comment.class,id);
        User user = genericRepository.findById(User.class, currentUser.getId());

        CommentVote commentVote = new CommentVote(user,comment, vote);
        genericRepository.merge(commentVote);
    }
}
