package usecase.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.entity.Post;

/**
 * Classe DTO relativa alla modifica di un post.
 */
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostEditDTO {

    private String title;
    private String content;
    private Post.Type type;
}
