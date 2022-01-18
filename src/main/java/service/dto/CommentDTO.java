package service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class CommentDTO {
    private final int id;
    private final String username;
    private final String content;
    private final String sectionName;
    private final Instant creationTime;
    private final int parentCommentId = 0;
}
