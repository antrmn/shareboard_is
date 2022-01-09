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

    private Integer id;
    private String title;
    private Integer vote;
    private Integer votes;
    private SectionLite section;
    private UserLite author;
    private String content;
    private Integer nComments;
}
