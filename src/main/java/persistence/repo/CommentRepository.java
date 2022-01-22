package persistence.repo;

import persistence.model.Comment;
import persistence.model.Post;

import java.util.List;

public class CommentRepository extends AbstractRepository<Comment, Integer> {
    public CommentRepository() {
        super(Comment.class);
    }

    public List<Comment> getByPost(Post post, int depth){
        return em.createQuery(
                        "from Comment c where c.post = :post and length(c.path) <= (7 * :depth) + 1 order by c.path",
                        Comment.class)
                .setParameter("depth", depth)
                .setParameter("post", post)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }

    public List<Comment> getReplies(Comment comment, int depth){
        return em.createQuery(
                    "from Comment c where c.parentComment = :parent and length(c.path) <= (7 * :depth) + 1 " +
                            "order by c.path",
                    Comment.class)
                .setParameter("depth", depth)
                .setParameter("parent", comment)
                .setHint("org.hibernate.readOnly", true)
                .getResultList();
    }
}
