package persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Ban implements Serializable {

    @Getter @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Getter @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    protected User user;

    @Getter
    @Column(nullable = false, insertable = false, updatable = false)
    protected Instant startTime; //db generated column

    @Getter @Setter
    @Column(nullable = true)
    protected Instant endTime;

    public Ban(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ban)) return false;
        Ban ban = (Ban) o;
        return id.equals(ban.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
