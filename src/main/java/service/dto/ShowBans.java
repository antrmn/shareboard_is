package service.dto;

import model.user.User;

import java.time.Instant;

public class ShowBans {

    private Integer id;
    private User admin;
    private SectionID section;
    private Instant startTime;
    private Instant endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public SectionID getSection() {
        return section;
    }

    public void setSection(SectionID section) {
        this.section = section;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}
