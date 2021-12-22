package persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "post_id")
    protected Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "author_id")
    protected User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    //@JoinColumn(name = "parent_comment_id")
    protected Comment parentComment;

    @Column(columnDefinition = "TEXT", nullable = false)
    protected String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant creationDate;

    @Column(insertable = false, updatable = false)
    protected Integer votes;

    @Column(insertable = false, updatable = false)
    protected String path;

    /* -- */

    public Integer getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getAuthor() {
        return author;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }
}
