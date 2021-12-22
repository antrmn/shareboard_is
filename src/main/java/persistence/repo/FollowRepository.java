package persistence.repo;

import persistence.model.Follow;
import persistence.model.User;

import java.util.List;

public class FollowRepository extends GenericRepository<Follow, Follow.Id>{
    public FollowRepository() {
        super(Follow.class);
    }

    List<Follow> getByUser(User user){
        return em.createQuery("from Follow f where f.id.user=:user", Follow.class)
                .setParameter("user", user)
                .getResultList();
    }

    List<Follow> getBySection(User user){
        return em.createQuery("from Follow f where f.id.user=:user", Follow.class)
                .setParameter("user", user)
                .getResultList();
    }
}
