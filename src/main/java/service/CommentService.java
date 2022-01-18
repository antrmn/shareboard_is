package service;

import persistence.model.Comment;
import persistence.repo.CommentRepository;
import persistence.repo.UserRepository;
import persistence.model.User;
import persistence.repo.*;
import service.dto.CommentDTO;
import service.dto.CommentTreeDTO;
import service.dto.LoggedInUser;
import service.validation.CommentExists;
import service.validation.PostExists;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


@Stateless
@Service
public class CommentService {
    @Inject private CommentRepository commentRepo;
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private LoggedInUser loggedInUser;

    private static final int MAX_COMMENT_DEPTH = 4;

    public CommentTreeDTO getPostComments(int postId){
        List<Comment> comments = commentRepo.getByPost(postRepo.findById(postId), MAX_COMMENT_DEPTH);
        return mapToTree(comments);
    }

    public CommentTreeDTO getReplies(int commentId){
        List<Comment> comments = commentRepo.getReplies(commentRepo.findById(commentId), MAX_COMMENT_DEPTH);
        return mapToTree(comments);
    }

    private CommentTreeDTO mapToTree(Collection<Comment> comments){
        Function<Comment, CommentDTO> mapper = x -> CommentDTO.builder()
                .id(x.getId())
                .username(x.getAuthor().getUsername())
                .content(x.getContent())
                .sectionName(x.getPost().getSection().getName())
                .creationTime(x.getCreationDate()).build();

        Stream<CommentDTO> commentDTOStream = comments.stream().map(mapper);
        return CommentTreeDTO.create(commentDTOStream);
    }

    public Comment getComment(@CommentExists int id){
        return commentRepo.findById(id);
    }

    @RolesAllowed({"user","admin"})
    @Transactional
    public void Delete(@CommentExists int id){
        commentRepo.remove(commentRepo.findById(id));
    }

    public void EditComment(@CommentExists int id, String text){
        commentRepo.findById(id).setContent(text);
    }

    @RolesAllowed({"user","admin"})
    @Transactional
    public int newComment(@NotBlank() @Size() String text,
                       @PostExists int postId){
        User user = userRepo.getByName(loggedInUser.getUsername());

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(postRepo.findById(postId));
        return commentRepo.insert(comment).getId();
    }

    @RolesAllowed({"user","admin"})
    @Transactional
    public int newCommentReply(@NotBlank() @Size() String text,
                               @CommentExists int parentId,
                               @PostExists int postId){
        User user = userRepo.getByName(loggedInUser.getUsername());
        Comment parent = commentRepo.findById(parentId);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(postRepo.findById(postId));
        comment.setParentComment(parent);
        return commentRepo.insert(comment).getId();
    }

}
