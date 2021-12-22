package persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class Ban implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    protected User user;

    @CreationTimestamp
    @Column(nullable = false, insertable = false, updatable = false)
    protected Instant startTime;

    @Column(nullable = true)
    protected Instant endTime;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}
