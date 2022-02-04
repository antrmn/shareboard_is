package persistence.repo;

import org.hibernate.Session;
import org.hibernate.SimpleNaturalIdLoadAccess;
import persistence.model.Section;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
