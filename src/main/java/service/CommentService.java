package service;

import persistence.model.Comment;
import persistence.model.User;
import persistence.repo.CommentRepository;
import persistence.repo.PostRepository;
import persistence.repo.UserRepository;
import service.auth.AuthenticationRequired;
import service.auth.DenyBannedUsers;
import service.dto.CommentDTO;
import service.dto.CommentTreeDTO;
import service.dto.CurrentUser;
import service.validation.CommentExists;
import service.validation.PostExists;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


@ApplicationScoped
@Transactional
public class CommentService {
    @Inject private CommentRepository commentRepo;
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private CurrentUser currentUser;

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

    @AuthenticationRequired
    public void delete(@CommentExists int id){
        commentRepo.remove(commentRepo.findById(id));
    }

    @AuthenticationRequired
    public void editComment(@CommentExists int id, String text){
        commentRepo.findById(id).setContent(text);
    }

    @AuthenticationRequired
    @DenyBannedUsers
    public int newComment(@NotBlank @Size String text,
                       @PostExists int postId){
        User user = userRepo.getByName(currentUser.getUsername());

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(postRepo.findById(postId));
        return commentRepo.insert(comment).getId();
    }

    @RolesAllowed({"user","admin"})
    @Transactional
    public int newCommentReply(@NotBlank @Size String text,
                               @CommentExists int parentId,
                               @PostExists int postId){
        User user = userRepo.getByName(currentUser.getUsername());
        Comment parent = commentRepo.findById(parentId);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(postRepo.findById(postId));
        comment.setParentComment(parent);
        return commentRepo.insert(comment).getId();
    }

}
