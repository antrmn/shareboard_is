package persistence.repo;

import persistence.model.Ban;
import persistence.model.User;

import java.time.Instant;
import java.util.List;

public class BanRepository extends GenericRepository<Ban, Integer>{
    public BanRepository() {
        super(Ban.class);
    }

    public List<Ban> getByUser(User user){
        return em.createQuery("from Ban b where b.user=:user",Ban.class).setParameter("user",user)
                .getResultList();
    }

    public List<Ban> getByUserCurrent(User user){
        return em.createQuery("from Ban b where b.user=:user and b.endTime < :now or b.endTime = null",
                Ban.class)
                .setParameter("now", Instant.now())
                .setParameter("user",user)
                .getResultList();
    }
}
