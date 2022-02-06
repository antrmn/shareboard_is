package service;

import persistence.model.Ban;
import persistence.model.Post;
import persistence.model.User;
import persistence.repo.GenericRepository;
import service.auth.AdminsOnly;
import service.dto.BanDTO;
import service.dto.PostPage;
import service.validation.BanExists;
import service.validation.UserExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Future;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class BanService {
    @Inject private GenericRepository genericRepository;



    @AdminsOnly
    public Ban addBan(@Future Instant date, @UserExists int userId) {
        Ban ban = new Ban();
        ban.setEndTime(date);
        User user = new User();
        user.setId(userId);
        ban.setUser(user);
        return genericRepository.insert(ban);
    }

    @AdminsOnly
    public void removeBan(@BanExists int banId){
        genericRepository.remove(genericRepository.findById(Ban.class, banId));
    }

    @AdminsOnly
    public List<BanDTO> retrieveUserBan(@UserExists int userId){
        User user = genericRepository.findById(User.class, userId);
        List<BanDTO> bans = new ArrayList<>();
        List<Ban> temp = user.getBans();

        for(Ban b : temp){
            BanDTO ban = new BanDTO(b.getId(), b.getEndTime());
            bans.add(ban);
        }
        return bans;
    }
}
