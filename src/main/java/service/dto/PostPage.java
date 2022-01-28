package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostPage {

    private int id;
    private String title;
    private int vote;
    private int votes;
    private String sectionName;
    private String authorName;
    private Integer sectionId;
    private Integer authorId;
    private String content;
    private int nComments;
}
