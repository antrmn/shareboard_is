package persistence.repo;

import org.hibernate.SimpleNaturalIdLoadAccess;
import persistence.model.Follow;
import persistence.model.Section;
import org.hibernate.Session;
import persistence.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class SectionRepository {

    @PersistenceContext
    protected EntityManager em;

    public Section getByName(String name){
        return getByName(name, false);
    }

    public Section getByName(String name, boolean getReference){
        SimpleNaturalIdLoadAccess<Section> user = em.unwrap(Session.class).bySimpleNaturalId(Section.class);
        return getReference ? user.getReference(name) : user.load(name);
    }
}
