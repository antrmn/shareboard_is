package persistence.repo;

import persistence.model.Follow;
import persistence.model.Section;
import persistence.model.User;

import java.util.List;

public class FollowRepository extends GenericRepository<Follow, Follow.Id>{
    public FollowRepository() {
        super(Follow.class);
    }

    public List<Follow> getByUser(User user){
        return em.createQuery("from Follow f where f.id.user=:user", Follow.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Follow getBySection(Section section){
        return em.createQuery("from Follow f where f.id.section=:section", Follow.class)
                .setParameter("section", section)
                .getSingleResult();
    }
}
