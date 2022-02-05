package service;

import persistence.model.Comment;
import persistence.model.CommentVote;
import persistence.model.Post;
import persistence.model.User;
import persistence.repo.CommentRepository;
import persistence.repo.GenericRepository;
import service.auth.AuthenticationRequired;
import service.auth.DenyBannedUsers;
import service.dto.CommentDTO;
import service.dto.CurrentUser;
import service.validation.CommentExists;
import service.validation.PostExists;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@ApplicationScoped
@Transactional
public class CommentService {
    @Inject private GenericRepository genericRepository;
    @Inject private CommentRepository commentRepo;
    @Inject private CurrentUser currentUser;

    private static final int MAX_COMMENT_DEPTH = 4;

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
                .votes(comment.getVotesCount())
                .creationDate(comment.getCreationDate())
                .parentCommentId(comment.getParentComment() == null ? 0 : comment.getParentComment().getId())
                .build();
    }

    public Map<Integer,List<CommentDTO>> getPostComments(int postId){
        List<Comment> comments = commentRepo.getByPost(genericRepository.findById(Post.class, postId), 
                MAX_COMMENT_DEPTH);
        return comments.stream().map(this::map).collect(groupingBy(CommentDTO::getParentCommentId, toList()));
    }

    public Map<Integer,List<CommentDTO>> getReplies(int commentId){
        List<Comment> comments = commentRepo.getReplies(genericRepository.findById(Comment.class, commentId), MAX_COMMENT_DEPTH);
        return comments.stream().map(this::map).collect(groupingBy(CommentDTO::getParentCommentId, toList()));
    }


    public Comment getComment(@CommentExists int id){
        return genericRepository.findById(Comment.class, id);
    }

    @AuthenticationRequired
    public void delete(@CommentExists int id){
        genericRepository.remove(genericRepository.findById(Comment.class, id));
    }

    @AuthenticationRequired
    public void editComment(@CommentExists int id, String text){
        genericRepository.findById(Comment.class, id).setContent(text);
    }

    @AuthenticationRequired
    @DenyBannedUsers
    public int newComment(@NotBlank @Size String text,
                       @PostExists int postId){
        User user = genericRepository.findById(User.class, currentUser.getId());

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(genericRepository.findById(Post.class, postId));
        return genericRepository.insert(comment).getId();
    }

    @RolesAllowed({"user","admin"})
    @Transactional
    public int newCommentReply(@NotBlank @Size String text,
                               @CommentExists int parentId,
                               @PostExists int postId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Comment parent = genericRepository.findById(Comment.class, parentId);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(text);
        comment.setPost(genericRepository.findById(Post.class, postId));
        comment.setParentComment(parent);
        return genericRepository.insert(comment).getId();
    }

}
