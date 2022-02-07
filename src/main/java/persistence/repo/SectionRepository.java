package persistence.repo;

import org.hibernate.Session;
import org.hibernate.SimpleNaturalIdLoadAccess;
import persistence.model.Section;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class SectionRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    public Section getByName(String name){
        return getByName(name, false);
    }

    public Section getByName(String name, boolean getReference){
        SimpleNaturalIdLoadAccess<Section> user = em.unwrap(Session.class).bySimpleNaturalId(Section.class);
        return getReference ? user.getReference(name) : user.load(name);
    }

    public List<Section> getMostFollowedSections(){
        return em.createQuery("from Section s order by follows.size desc", Section.class).getResultList();
    }

    public List<Section> getMostFollowedSections(Instant after){
        return em.createQuery(
                "select s from Section s left join s.follows f where f.followDate >= :after " +
                "group by s " +
                "order by count(f) desc", Section.class)
                .setParameter("after",after).getResultList();
    }
}
