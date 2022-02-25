package service;

import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.GenericRepository;
import service.auth.AdminsOnly;
import service.dto.BanDTO;
import service.validation.BanExists;
import service.validation.UserExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class BanService {
    private GenericRepository genericRepository;

    protected BanService(){}

    @Inject
    protected BanService(GenericRepository genericRepository){
        this.genericRepository = genericRepository;
    }


    /**
     * Aggiunge un ban dato l'id di un utente.
     * @param date data di fine ban
     * @param userId identificativo utente
     * @return entità ban aggiunta
     */
    @AdminsOnly
    public int addBan(@NotNull @Future Instant date, @UserExists int userId) {
        Ban ban = new Ban();
        ban.setEndTime(date);
        User user = new User();
        user.setId(userId);
        ban.setUser(user);
        return genericRepository.insert(ban).getId();
    }

    /**
     * Rimuove un ban dato il suo id
     * @param banId identificativo del ban
     */
    @AdminsOnly
    public void removeBan(@BanExists int banId){
        genericRepository.remove(genericRepository.findById(Ban.class, banId));
    }

    /**
     * Ritorna la lista dei ban di un utente
     * @param userId identificativo utente
     * @return lista dei ban relativi all'utente
     */
    @AdminsOnly
    public List<BanDTO> retrieveUserBan(@UserExists int userId){
        User user = genericRepository.findById(User.class, userId);
        List<BanDTO> bans = new ArrayList<>();
        List<Ban> temp = user.getBans();

        for(Ban b : temp){
            BanDTO ban = new BanDTO(b.getId(), b.getEndTime(), b.getUser().getId());
            bans.add(ban);
        }
        return bans;
    }
}
