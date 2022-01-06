package service;

import persistence.EntityManagerProducer;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.exception.BadRequestException;
import service.validation.UserExists;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BanService {
    @Inject private BanRepository banRepo;
    @Resource private EJBContext ctx;

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
    public void removeBan(int banId){
        Ban ban = banRepo.findById(banId);
        if(ban != null){
            banRepo.remove(ban);
        }else{
            throw new BadRequestException("Ban non trovato");
        }
    }
}
