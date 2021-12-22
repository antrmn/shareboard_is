package service.dto;

import model.post.Post;

import java.time.Instant;

public class PostPreview {

    private Integer id;
    private String title;
    private Integer vote;
    private Integer votes;
    private Post.Type type;
    private String content;
    private Instant creationDate;
    private Integer nComments;
    private SectionPostPreview section;
    private UserPostPreview author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Post.Type getType() {
        return type;
    }

    public void setType(Post.Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getnComments() {
        return nComments;
    }

    public void setnComments(Integer nComments) {
        this.nComments = nComments;
    }

    public SectionPostPreview getSection() {
        return section;
    }

    public void setSection(SectionPostPreview section) {
        this.section = section;
    }

    public UserPostPreview getAuthor() {
        return author;
    }

    public void setAuthor(UserPostPreview author) {
        this.author = author;
    }
}
