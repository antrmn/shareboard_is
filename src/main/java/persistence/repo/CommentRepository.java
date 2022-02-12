package persistence.repo;

import persistence.model.Comment;
import persistence.model.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Interfaccia usata per interagire con {@link javax.persistence.EntityManager}
 * Istanze che implementano questa interfaccia fungono da facciata alle funzionalità di JPA, fornendo i metodi
 * essenziali (CRUD) per una specifica entità di persistenza
 * @param <T> La classe delle entità di persistenza gestite da classi che implementano questa interfaccia
 * @param <ID> La classe della chiave primaria delle entità di persistenza gestite da classi che implementano questa
 *            interfaccia
 * @see javax.persistence.EntityManager
 */
public class CommentRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     *  Trova tutti i commenti di un determinato post
     * @param post entità Post di cui si vuole ottenere i commenti
     * @param depth profondità della ricorsione per le risposte ai post
     * @return Lista di commenti
     */
    public List<Comment> getByPost(Post post, int depth){
        return em.createQuery(
                        "from Comment c where c.post = :post and length(c.path) <= (7 * :depth) + 1 order by c.path",
                        Comment.class)
                .setParameter("depth", depth)
                .setParameter("post", post)
                .getResultList();
    }

    /**
     *  Trova tutti i commenti di riposta a un determinato commento
     * @param comment Entità Comment di cui si vuole ottenere i commenti
     * @param depth profondità della ricorsione per le risposte ai post
     * @return Lista di commenti
     */
    public List<Comment> getReplies(Comment comment, int depth){
        return em.createQuery(
                    "from Comment c where c.path like :path and length(c.path) <= (7 * :depth) + 1 order by c.path",
                    Comment.class)
                .setParameter("depth", depth)
                .setParameter("path", comment.getPath() + '%')
                .getResultList();
    }
}