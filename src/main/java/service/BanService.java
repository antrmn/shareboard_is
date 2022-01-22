package service;

import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.auth.AdminsOnly;
import service.validation.BanExists;
import service.validation.UserExists;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Future;
import java.time.Instant;
import java.util.List;

@Stateless
@Transactional
public class BanService {
    @Inject private BanRepository banRepo;
    @Inject private UserRepository userRepo;

    @AdminsOnly
    public void addBan(@Future Instant date, @UserExists int userId) {
        Ban ban = new Ban();
        ban.setEndTime(date);
        User user = new User();
        user.setId(userId);
        ban.setUser(user);
        banRepo.insert(ban);
    }

    @AdminsOnly
    public void removeBan(@BanExists int banId){
        banRepo.remove(banRepo.findById(banId));
    }

    @AdminsOnly
    public List<Ban> retrieveUserBan(@UserExists int userId){
        User user = userRepo.findById(userId);
        return banRepo.getByUser(user);
    }
}
