package persistence.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@DynamicUpdate
public class Post implements Serializable {
    public enum Type {TEXT, IMG}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "section_id")
    protected Section section;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "author_id")
    protected User author;

    @Column(length = 255, nullable = false)
    protected String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    protected String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Type type;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant creationDate;

    @Column(insertable = false, updatable = false)
    protected Integer votes;

    /* -- */

    public Integer getId() {
        return id;
    }

    public Section getSection() {
        return section;
    }

    public User getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
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

    public void setSection(Section section) {
        this.section = section;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
