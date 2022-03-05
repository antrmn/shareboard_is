package domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * Entità rappresentante il commento ad un post
 */
@Entity
public class Comment implements Serializable {

    @Setter @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Setter @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Post post;

    @Setter @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected User author;

    /**
     * <p>Riferimento al commento padre (o <pre>null</pre> se il commento in questione è un commento radice</p>
     */
    @Setter @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    protected Comment parentComment;

    @Setter @Getter
    @Column(columnDefinition = "TEXT", nullable = false)
    protected String content;

    @Getter
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant creationDate;

    @Getter
    @Column(name="votes", insertable = false, updatable = false)
    protected Integer votesCount;

    /**
     * <p>Il percorso materializzato del commento, contenente tutti gli ID (in base 36) dei commenti che, da
     * sinistra verso destra, costituiscono il percorso per arrivare al commento in questione a partire dalla radice.</p>
     */
    @Getter
    @Column(insertable = false, updatable = false)
    protected String path;

    @OneToMany(mappedBy="comment")
    @MapKeyJoinColumn(name="user_id", updatable = false, insertable = false)
    protected Map<User, CommentVote> votes;

    /**
     * Ottieni il voto di un utente al commento in questione (o <pre>null</pre> se il voto non è presente)
     * @param user Riferimento all'utente
     * @return Un oggetto {@link CommentVote}
     */
    public CommentVote getVote(User user){
        return votes.get(user);
    }

    public Comment(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
