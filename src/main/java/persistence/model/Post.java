package persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@DynamicUpdate
@ToString
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

    @Getter
    @Formula("(select count(c.id) from Comment c where c.post_id = id group by c.post_id)") //native sql ew
    protected Integer commentCount;

    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Section section;

    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected User author;

    @OneToMany(mappedBy="post")
    @MapKeyJoinColumn(name="user_id", updatable = false, insertable = false)
    protected Map<User, PostVote> votes = new HashMap<>();
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
