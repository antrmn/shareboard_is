package persistence.repo;

import persistence.model.Comment;
import persistence.model.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public class CommentRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    public List<Comment> getByPost(Post post, int depth){
        return em.createQuery(
                        "from Comment c where c.post = :post and length(c.path) <= (7 * :depth) + 1 order by c.path",
                        Comment.class)
                .setParameter("depth", depth)
                .setParameter("post", post)
                .getResultList();
    }

    public List<Comment> getReplies(Comment comment, int depth){
        return em.createQuery(
                    "from Comment c where c.path like :path and length(c.path) <= (7 * :depth) + 1 order by c.path",
                    Comment.class)
                .setParameter("depth", depth)
                .setParameter("path", comment.getPath() + '%')
                .getResultList();
    }
}