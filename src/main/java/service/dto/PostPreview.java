package service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.model.Post;

import java.time.Instant;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostPreview {
    private Integer id;
    private String title;
    private Integer vote;
    private Integer votes;
    private Post.Type type;
    private String content;
    private Instant creationDate;
    private Integer nComments;
    private SectionPostPreview section;
    private UserPostPreview author;
}
