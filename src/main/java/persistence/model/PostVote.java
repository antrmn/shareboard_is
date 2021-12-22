package persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class PostVote implements Serializable {

    @Embeddable
    public static class Id implements Serializable{
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        //@JoinColumn(name = "user_id")
        protected User user;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        //@JoinColumn(name = "post_id")
        protected Post post;

        /* -- */

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return user.equals(id.user) && post.equals(id.post);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, post);
        }
    }

    @EmbeddedId
    protected Id id;

    @Column(nullable = false)
    protected Short vote;

    /* -- */

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Short getVote() {
        return vote;
    }

    public void setVote(Short vote) {
        this.vote = vote;
    }
}
