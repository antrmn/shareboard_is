package persistence.repo;

import org.hibernate.Session;
import org.hibernate.SimpleNaturalIdLoadAccess;
import persistence.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Interfaccia usata per interagire con {@link javax.persistence.EntityManager}
 * Istanze che implementano questa interfaccia fungono da facciata alle funzionalità di JPA, fornendo i metodi
 * essenziali (CRUD) per una specifica entità di persistenza
 * @see javax.persistence.EntityManager
 */
public class UserRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Restituisce i dati di un utente dato un nome
     * @param name nome dell'utente
     * @return entità User
     */
    public User getByName(String name) {
        return getByName(name, false);
    }

    /**
     * Restituisce i dati di un utente dato un nome
     * @param name nome dell'utente
     * @param getReference booleano per attivare lazy loading
     * @return entità User
     */
    public User getByName(String name, boolean getReference) {
        SimpleNaturalIdLoadAccess<User> user = em.unwrap(Session.class).bySimpleNaturalId(User.class);
        return getReference ? user.getReference(name) : user.load(name);
    }

    /**
     * Restituisce i dati di un utente data un email
     * @param email email dell'utente
     * @return entità User
     */
    public User getByEmail(String email) {
        return em.createQuery("from User u where u.email=:email", User.class).setParameter("email", email).getSingleResult();
    }
}
