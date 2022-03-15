package model.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Entità rappresentate una relazione "segui" tra un utente e una sezione
 */
@Entity
public class Follow implements Serializable {

    @Embeddable
    public static class Id implements Serializable{

        @Getter
        @Column(name = "user_id", nullable = false)
        protected int userId;

        @Getter
        @Column(name = "section_id", nullable = false)
        protected int sectionId;

        protected Id(){}

        public Id(int userId, int sectionId){
            this.userId = userId;
            this.sectionId = sectionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id id = (Id) o;
            return userId == id.userId && sectionId == id.sectionId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, sectionId);
        }
    }

    @Getter
    @EmbeddedId
    protected Id id = new Id();

    @Getter
    @ManyToOne(optional = false) @MapsId("userId")
    protected User user;
    public void setUser(User user){
        this.user = user;
        this.id.userId = user.getId();
    }

    @Getter
    @ManyToOne(optional = false) @MapsId("sectionId")
    protected Section section;
    public void setSection(Section section){
        this.section = section;
        this.id.sectionId = section.getId();
    }

    @Getter
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant followDate;

    protected Follow(){}

    public Follow(User user, Section section){
        this.user = user;
        this.section = section;
        this.id = new Id(user.getId(), section.getId());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follow)) return false;
        Follow follow = (Follow) o;
        return id.equals(follow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
