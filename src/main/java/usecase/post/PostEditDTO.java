package usecase.post;

import domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostEditDTO {

    private String title;
    private String content;
    private Post.Type type;
}
