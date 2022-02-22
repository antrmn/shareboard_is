package service;

import persistence.model.Comment;
import persistence.model.CommentVote;
import persistence.model.Post;
import persistence.model.User;
import persistence.repo.CommentRepository;
import persistence.repo.GenericRepository;
import service.auth.AuthenticationRequired;
import service.auth.AuthorizationException;
import service.auth.DenyBannedUsers;
import service.dto.CommentDTO;
import service.dto.CurrentUser;
import service.validation.CommentExists;
import service.validation.PostExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@ApplicationScoped
@Transactional
public class CommentService {
    private final GenericRepository genericRepository;
    private final CommentRepository commentRepo;
    private final CurrentUser currentUser;

    private static final int MAX_COMMENT_DEPTH = 4;

    @Inject
    protected CommentService(GenericRepository genericRepository, CommentRepository commentRepository,
                                            CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.commentRepo = commentRepository;
        this.currentUser = currentUser;
    }

    /**
     * Converte Comment in CommentDTO.
     * @param comment commento da convertire
     * @return commentDTO con i dati di comment
     */
    private CommentDTO map (Comment comment){
        CommentVote commentVote = null;
        if(currentUser.isLoggedIn()){
            User user = genericRepository.findById(User.class, currentUser.getId());
            commentVote = comment.getVote(user);
        }

        return CommentDTO.builder()
                .id(comment.getId())
                .authorUsername(comment.getAuthor().getUsername())
                .authorId(comment.getAuthor().getId())
                .creationDate(comment.getCreationDate())
                .postId(comment.getPost().getId())
                .vote(commentVote == null ? 0 : commentVote.getVote())
                .content(comment.getContent())
                .votes(comment.getVotesCount() == null ? 0 : comment.getVotesCount())
                .creationDate(comment.getCreationDate()  == null ? Instant.now() : comment.getCreationDate())
                .parentCommentId(comment.getParentComment() == null ? 0 : comment.getParentComment().getId())
                .build();
    }

    /**
     * Ritorna una mappa la cui chiave è l'id del commento padre e il valore una lista di CommentDTO
     * @param postId l'id di un post esistente di cui si vuole ottenere i commenti
     * @return mappa con i commenti del post
     */
    public Map<Integer,List<CommentDTO>> getPostComments(@PostExists int postId){
        List<Comment> comments = commentRepo.getByPost(genericRepository.findById(Post.class, postId), MAX_COMMENT_DEPTH);
        return comments.stream().map(this::map).collect(groupingBy(CommentDTO::getParentCommentId, toList()));
    }

    /**
     * Ritorna una mappa la cui chiave è l'id del commento padre e il valore una lista di CommentDTO
     * @param commentId l'id di un commento esistente di cui si vuole ottenere le risposte
     * @return mappa con le risposte al commento
     */
    public Map<Integer, List<CommentDTO>> getReplies(@CommentExists int commentId){
        List<Comment> comments = commentRepo.getReplies(genericRepository.findById(Comment.class, commentId), MAX_COMMENT_DEPTH);
        return comments.stream().map(this::map).collect(groupingBy(CommentDTO::getParentCommentId, toList()));
    }

    /**
     * Ritorna un commento dato il suo id
     * @param id l'id di un commento esistente
     * @return commento avente l'id specificato
     */
    public CommentDTO getComment(@CommentExists int id){
        return map(genericRepository.findById(Comment.class, id));
    }


    /**
     * Cancella un commento dato il suo id
     * @param id l'id di un commento esistente
     */
    @AuthenticationRequired
    public void delete(@CommentExists int id){
        genericRepository.remove(genericRepository.findById(Comment.class, id));
    }

    /**
     * Modifica un commento dato il suo id
     * @param id l'id di un commento esistente
     * @param text stringa con testo da sostituire
     */
    @AuthenticationRequired
    public void editComment(@CommentExists int id, @NotBlank @Size(max=65535) String text){
        Comment comment = genericRepository.findById(Comment.class, id);
        if(currentUser.getId() != comment.getAuthor().getId() && !currentUser.isAdmin())
            throw new AuthorizationException();
        comment.setContent(text);
    }

    /**
     * Crea un nuovo commento e ne restituisce l'id
     * @param text una stringa non vuota di massimo 65535 caratteri
     * @param postId id di un post esistente
     * @return id del commento creato
     */
    @AuthenticationRequired
    @DenyBannedUsers
    public int newComment(@NotBlank @Size(max=65535) String text,
                          @PostExists int postId){
        User user = genericRepository.findById(User.class, currentUser.getId());

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(genericRepository.findById(Post.class, postId));
        return genericRepository.insert(comment).getId();
    }

    /**
     * Crea una risposta a un commento e ne restituisce l'id
     * @param text una stringa non vuota di massimo 1000 caratteri
     * @param parentCommentId id di un commento esistente
     * @return id del commento creato
     */
    @AuthenticationRequired
    public int newCommentReply(@NotBlank @Size(max=65535) String text,
                               @CommentExists int parentCommentId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Comment parent = genericRepository.findById(Comment.class, parentCommentId);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(parent.getPost());
        comment.setParentComment(parent);
        return genericRepository.insert(comment).getId();
    }

}
