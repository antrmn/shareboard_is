package persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Follow implements Serializable {
    @Embeddable
    public static class Id implements Serializable{
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        //@JoinColumn(name = "user_id")
        protected User user;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        //@JoinColumn(name = "section_id")
        protected Section section;

        /* -- */

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Section getSection() {
            return section;
        }

        public void setSection(Section section) {
            this.section = section;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return user.equals(id.user) && section.equals(id.section);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, section);
        }
    }

    @EmbeddedId
    protected Id id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false)
    protected Instant followDate;

    /* -- */

    public Id getId() {
        return id;
    }

    public Instant getFollowDate() {
        return followDate;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
