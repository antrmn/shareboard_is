package service;

import persistence.EntityManagerProducer;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.exception.BadRequestException;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
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
    public void addBan(String date, int userId) throws BadRequestException, PersistenceException {
        if(!ctx.isCallerInRole("admin")){ //controllo necessario? visto che abbiamo @RolesAllowed
            throw new BadRequestException("Non sei autorizzato ad eseguire questa operazione");
        }
        Instant currentDate = Instant.now();
        Instant endDate;
        if (date!= null && !date.isEmpty()){
            endDate = LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC);

            if(currentDate.isAfter(endDate)){
                throw new BadRequestException("La data non può essere antecedente a quella attuale");
            }
        } else{
            throw new BadRequestException("Data non valida");
        }
        Ban ban = new Ban();
        ban.setEndTime(endDate);
        User user = new User();
        user.setId(userId);
        ban.setUser(user);
        banRepo.insert(ban);
    }

    @RolesAllowed({"admin"})
    @Transactional
    public void removeBan(int banId){
        if(!ctx.isCallerInRole("admin")){ //controllo necessario? visto che abbiamo @RolesAllowed
            throw new BadRequestException("Non sei autorizzato ad eseguire questa operazione");
        }
        Ban ban = banRepo.findById(banId);
        if(ban != null){
            banRepo.remove(ban);
        }else{
            throw new BadRequestException("Ban non trovato");
        }
    }
}
