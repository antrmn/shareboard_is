package persistence.model;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Section implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(length = 255)
    protected String description;

    @NaturalId(mutable = true)
    @Column(length = 50, nullable = false, unique = true)
    protected String name;

    @Column(length = 4096)
    protected String picture;

    @Column(length = 4096)
    protected String banner;

    @Formula(value = "")
    protected Integer nFollowers;

    /* -- */

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getBanner() {
        return banner;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
