package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.model.Post;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostEditDTO {

    private String title;
    private String content;
    private Post.Type type;
}
