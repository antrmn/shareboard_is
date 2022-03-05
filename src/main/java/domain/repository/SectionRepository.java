package domain.repository;

import domain.entity.Section;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;


/**
 * Classe che incapsula la logica per il recupero di entità di tipo {@link Section}
 */
public class SectionRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

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
                "select s from Section s right join s.follows f where f.followDate >= :after " +
                "group by s " +
                "order by count(f) desc", Section.class)
                .setParameter("after",after).getResultList();
    }
}
