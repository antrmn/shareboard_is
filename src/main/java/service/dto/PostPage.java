package service.dto;

import model.section.Section;
import model.user.User;

public class PostPage {

    private Integer id;
    private String title;
    private Integer vote;
    private Integer votes;
    private SectionLite section;
    private UserLite author;
    private String content;
    private Integer nComments;

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

    public SectionLite getSection() {
        return section;
    }

    public void setSection(SectionLite section) {
        this.section = section;
    }

    public UserLite getAuthor() {
        return author;
    }

    public void setAuthor(UserLite author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getnComments() {
        return nComments;
    }

    public void setnComments(Integer nComments) {
        this.nComments = nComments;
    }
}
