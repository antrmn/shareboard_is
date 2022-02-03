package persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity
public class User implements Serializable {

    @Getter @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Getter @Setter
    @NaturalId(mutable = false) @Column(length = 30, unique = true, nullable = false)
    protected String username;

    @Getter @Setter
    @Column(length = 16, nullable = false)
    protected byte[] password;

    @Getter @Setter
    @Column(length = 16, nullable = false)
    protected byte[] salt;

    @Getter @Setter
    @Column(length = 255, unique = true, nullable = false)
    protected String email;

    @Getter @Setter
    @Column(length = 255)
    protected String description;

    @Getter @Setter
    @Column(length = 4096)
    protected String picture;

    @Getter
    @CreationTimestamp @Column(insertable = false, updatable = false, nullable = false)
    protected Instant creationDate;

    @Getter @Setter
    @Column(nullable = false)
    protected Boolean admin;

    @OneToMany(mappedBy = "user")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @OrderBy("endTime desc")
    protected List<Ban> bans = new ArrayList<>();
    public List<Ban> getBans(){
        return Collections.unmodifiableList(bans);
    }

    public User(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
