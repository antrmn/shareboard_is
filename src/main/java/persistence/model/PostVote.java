package persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class PostVote implements Serializable {

    @SuppressWarnings("JpaDataSourceORMInspection") //bug IDEA-223439
    @Embeddable
    public static class Id implements Serializable{

        @Getter
        @Column(name = "user_id", nullable = false)
        protected int userId;

        @Getter
        @Column(name = "post_id", nullable = false)
        protected int postId;

        protected Id(){}

        public Id(int userId, int postId) {
            this.userId = userId;
            this.postId = postId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostVote.Id)) return false;
            PostVote.Id id = (PostVote.Id) o;
            return userId == id.userId && postId == id.postId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, postId);
        }
    }

    @Getter
    @EmbeddedId
    protected PostVote.Id id = new PostVote.Id();

    @Getter
    @ManyToOne(optional = false) @MapsId("postId")
    protected Post post;
    public void setPost(Post post){
        this.post = post;
        this.id.postId = post.getId();
    }

    @Getter
    @ManyToOne(optional = false) @MapsId("userId")
    protected User user;
    public void setUser(User user){
        this.user = user;
        this.id.userId = user.getId();
    }

    @Setter
    @Getter
    @Column(nullable = false)
    protected Short vote;

    protected PostVote(){}

    public PostVote(User user, Post post, Short vote) {
        this.user = user;
        this.post = post;
        this.vote = vote;
        this.id = new PostVote.Id(user.getId(), post.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostVote)) return false;
        PostVote that = (PostVote) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
