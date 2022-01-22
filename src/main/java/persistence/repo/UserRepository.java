package persistence.repo;

import persistence.model.User;
import org.hibernate.Session;

public class UserRepository extends AbstractRepository<User, Integer> {
    public UserRepository() {
        super(User.class);
    }

    public User getByName(String name){
        //unwrap serve ad ottenere hibernate.session a partire da javax.persistence.EntityManager
        return em.unwrap(Session.class).bySimpleNaturalId(User.class).load(name);
    }

    public User getByEmail(String email){
        return em.createQuery("from User u where u.email=:email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
