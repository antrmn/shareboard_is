package domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
public class CommentVote implements Serializable {

    @SuppressWarnings("JpaDataSourceORMInspection") //bug IDEA-223439
    @Embeddable
    public static class Id implements Serializable{

        @Getter
        @Column(name = "user_id", nullable = false)
        protected int userId;

        @Getter
        @Column(name = "comment_id", nullable = false)
        protected int commentId;

        protected Id(){}

        public Id(int userId, int commentId) {
            this.userId = userId;
            this.commentId = commentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id id = (Id) o;
            return userId == id.userId && commentId == id.commentId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, commentId);
        }
    }

    @Getter
    @EmbeddedId
    protected Id id = new Id();

    @Getter
    @ManyToOne(optional = false) @MapsId("commentId")
    protected Comment comment;
    public void setComment(Comment comment){
        this.comment = comment;
        this.id.commentId = comment.getId();
    }

    @Getter
    @ManyToOne(optional = false) @MapsId("userId")
    protected User user;
    public void setUser(User user){
        this.user = user;
        this.id.userId = user.getId();
    }

    @Setter @Getter
    @Column(nullable = false)
    protected Short vote;

    protected CommentVote(){}

    public CommentVote(User user, Comment comment, Short vote) {
        this.user = user;
        this.comment = comment;
        this.vote = vote;
        this.id = new Id(user.getId(), comment.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentVote)) return false;
        CommentVote that = (CommentVote) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
