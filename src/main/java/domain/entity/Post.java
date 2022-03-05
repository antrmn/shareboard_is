package domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entità rappresentante un post
 */
@Entity
@DynamicUpdate
public class Post implements Serializable {
    public enum Type {TEXT, IMG}

    @Getter @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Setter @Getter
    @Column(length = 255, nullable = false)
    protected String title;

    @Getter @Setter
    @Column(columnDefinition = "TEXT", nullable = false)
    protected String content;

    @Setter @Getter
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    protected Type type;

    @Getter
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant creationDate; //generato da sql

    @Getter
    @Column(name = "votes", nullable = false, insertable = false, updatable = false)
    protected Integer votesCount;

    @OneToMany(mappedBy="post")
    @LazyCollection(LazyCollectionOption.EXTRA)
    protected List<Comment> comments;
    public int getCommentCount(){
        return comments == null ? 0 : comments.size();
    }

    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Section section;

    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected User author;

    @OneToMany(mappedBy="post")
    @MapKeyJoinColumn(name="user_id", updatable = false, insertable = false)
    protected Map<User, PostVote> votes = new HashMap<>();

    /**
     * Ottieni il voto di un utente al post in questione (o <pre>null</pre> se il voto non è presente)
     * @param user Riferimento all'utente
     * @return Un oggetto {@link PostVote}
     */
    public PostVote getVote(User user){
        return votes.get(user);
    }

    public Post(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return id != null && id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
