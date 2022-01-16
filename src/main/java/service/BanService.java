package service;

import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.exception.BadRequestException;
import service.validation.BanExists;
import service.validation.UserExists;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.constraints.Future;
import java.time.Instant;
import java.util.List;

@Stateless
public class BanService {
    @Inject private BanRepository banRepo;
    @Inject private UserRepository userRepo;

    @RolesAllowed({"admin"})
    @Transactional
    public void addBan(@Future Instant date, @UserExists int userId) throws BadRequestException, PersistenceException {
        Ban ban = new Ban();
        ban.setEndTime(date);
        User user = new User();
        user.setId(userId);
        ban.setUser(user);
        banRepo.insert(ban);
    }

    @RolesAllowed({"admin"})
    @Transactional
    public void removeBan(@BanExists int banId){
        banRepo.remove(banRepo.findById(banId));
    }

    @RolesAllowed({"admin"})
    @Transactional
    public List<Ban> retrieveUserBan(@UserExists int userId){
        User user = userRepo.findById(userId);
        return banRepo.getByUser(user);
    }
}
