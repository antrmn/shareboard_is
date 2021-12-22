package persistence.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NaturalId(mutable = false)
    @Column(length = 30, unique = true, nullable = false)
    protected String username;

    @Column(length = 16, nullable = false)
    protected byte[] password;

    @Column(length = 16, nullable = false)
    protected byte[] salt;

    @Column(length = 255, unique = true, nullable = false)
    protected String email;

    @Column(length = 255)
    protected String description;

    @Column(length = 4096)
    protected String picture;

    @CreationTimestamp
    @Column(insertable = false, updatable = false, nullable = false)
    protected Instant creationDate;

    @Column(nullable = false)
    protected Boolean admin;

    /* -- */

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
