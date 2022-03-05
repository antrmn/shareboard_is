package domain.repository;

import domain.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;


/**
 * Classe che incapsula la logica per il recupero di entità di tipo {@link User}
 */
public class UserRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Restituisce i dati di un utente data un email
     * @param email email dell'utente
     * @return entità User
     */
    public User getByEmail(String email) {
        return em.createQuery("from User u where u.email=:email", User.class).setParameter("email", email)
                .getResultList().stream().findFirst().orElse(null);
    }
}
