package usecase.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data @Builder
@AllArgsConstructor
public class CommentDTO {
    private final int id;
    private final String authorUsername;
    private final int authorId;
    private final String content;
    private final Instant creationDate;
    @Builder.Default private final int parentCommentId = 0;
    private final int postId;
    private final int vote;
    private final int votes;
}
