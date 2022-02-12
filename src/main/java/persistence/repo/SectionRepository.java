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

    /**
     * Restituisce i dati di una sezione dato un nome
     * @param name nome della sezione
     * @return entità Section
     */
    public Section getByName(String name){
        return getByName(name, false);
    }

    /**
     * Restituisce i dati di una sezione dato un nome
     * @param name nome della sezione
     * @param getReference booleano per attivare lazy loading
     * @return entità Section
     */
    public Section getByName(String name, boolean getReference){
        SimpleNaturalIdLoadAccess<Section> section = em.unwrap(Session.class).bySimpleNaturalId(Section.class);
        return getReference ? section.getReference(name) : section.load(name);
    }

    /**
     * Restituisce una lista con i dati delle sezioni con più follows
     * @return lista di entità Section delle sezioni con più follows
     */
    public List<Section> getMostFollowedSections(){
        return em.createQuery("from Section s order by follows.size desc", Section.class).getResultList();
    }

    /**
     * Restituisce una lista con i dati delle sezioni con più follows in un dato arco di tempo
     * @param after data dopo la quale contare i follows delle sezioni
     * @return lista di entità Section delle sezioni con più follows
     */
    public List<Section> getMostFollowedSections(Instant after){
        return em.createQuery(
                "select s from Section s left join s.follows f where f.followDate >= :after " +
                "group by s " +
                "order by count(f) desc", Section.class)
                .setParameter("after",after).getResultList();
    }
}
