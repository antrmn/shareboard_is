package model.repository;

import org.hibernate.Session;
import org.hibernate.SimpleNaturalIdLoadAccess;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * Classe che incapsula la logica CRUD di base per tutte le entità persistenti
 * @see javax.persistence.EntityManager
 */
public class GenericRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     *  Trova per chiave primaria
     * @return L'entità trovata o null se l'entità non esiste
     * @throws IllegalArgumentException se la chiave primaria fornita è null o non valida per l'entità da cercare
     * @see javax.persistence.EntityManager#find(Class, Object)
     */
    public <T> T findById(Class<T> entityClass, Object primaryKey, boolean loadLazily) {
        return loadLazily ? em.getReference(entityClass, primaryKey) :
                            em.find(entityClass, primaryKey, LockModeType.NONE);
    }

    /**
     * Trova per chiave primaria, restituendo un'entità caricata pigramente (lazy-loaded). L'entità restituita viene
     * caricata al primo accesso effettuato. Se l'istanza non esiste, viene lanciato {@link javax.persistence.EntityNotFoundException}
     * quando l'istanza viene acceduta per la prima volta.
     * @return L'entità caricata pigramente
     * @throws IllegalArgumentException se la chiave primaria fornita è null o non valida per l'entità da cercare
     * @throws javax.persistence.EntityNotFoundException se l'entità non può essere acceduta
     * @see javax.persistence.EntityManager#getReference(Class, Object)
     */
    public <T> T findById(Class<T> entityClass, Object primaryKey){
        return findById(entityClass,primaryKey,false);
    }

    public <T> T findByNaturalId(Class<T> entityClass, Object naturalKey) {
        return findByNaturalId(entityClass,naturalKey,false);
    }

    public <T> T findByNaturalId(Class<T> entityClass, Object naturalKey, boolean getReference) {
        SimpleNaturalIdLoadAccess<T> entity = em.unwrap(Session.class).bySimpleNaturalId(entityClass);
        return getReference ? entity.getReference(naturalKey) : entity.load(naturalKey);
    }

    /**
     * Restituisce una lista tipata contenente tutte le istanze di una determinata entità di persistenza
     * @return La lista dei risultati
     * @throws javax.persistence.QueryTimeoutException In caso di timeout raggiunto
     * @throws javax.persistence.PersistenceException
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        CriteriaQuery<T> c = em.getCriteriaBuilder().createQuery(entityClass);
        c.select(c.from(entityClass));
        return em.createQuery(c).getResultList();
    }

    /**
     * Restituisce il numero indicante la quantità di istanze di una determinata entità di persistenza
     * @return La quantità di istanze
     * @throws javax.persistence.QueryTimeoutException In caso di timeout raggiunto
     * @throws javax.persistence.PersistenceException
     */
    public <T> Long getCount(Class<T> entityClass) {
        CriteriaQuery<Long> c =
                em.getCriteriaBuilder().createQuery(Long.class);
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)));
        return em.createQuery(c).getSingleResult();
    }

    /**
     * Crea un'istanza persistente e gestita (managed entity)
     * @param entity L'istanza di entità
     * @return L'istanza passata come parametro
     * @throws javax.persistence.EntityExistsException se l'entità esiste già
     * @see javax.persistence.EntityManager#persist(Object)
     */
    public <T> T insert(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    /**
     * Unisce lo stato di una data entità allo stato nel contesto di persistenza
     * @param entity
     * @return Una copia <i>managed</i> dell'entità passata come parametro
     * @see javax.persistence.EntityManager#merge(Object)
     */
    public <T> T merge(T entity) {
        return em.merge(entity);
    }

    /**
     * Rimuove l'istanza dal contesto di persistenza
     * @param entity L'entità managed da rimuovere
     * @throws IllegalArgumentException se l'entità passata non è managed
     */
    public <T> void remove(T entity) {
        em.remove(entity);
    }
}
