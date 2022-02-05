package service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data @Builder
public class PostPage {
    private int id;
    private String title;
    private int vote;
    private int votes;
    private String sectionName;
    private String authorName;
    private Integer sectionId;
    private Integer authorId;
    private Instant creationDate;
    private String content;
    private int nComments;
    private PostType type;
}
