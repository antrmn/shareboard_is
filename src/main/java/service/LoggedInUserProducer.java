package service;

import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.dto.LoggedInUser;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.security.Principal;
import java.time.Instant;
import java.util.Comparator;

public class LoggedInUserProducer {
    @Inject private Principal principal;
    @Inject UserRepository userRepo;
    @Inject BanRepository banRepo;

    @RequestScoped
    @Produces
    public LoggedInUser produceLoggedInUser(){
        if(principal == null || principal.getName() == null)
            return new LoggedInUser();

        User user = userRepo.getByName(principal.getName());
        if(user == null)
            return new LoggedInUser();

        Instant longestCurrentBan = banRepo.getByUserCurrent(user).stream()
                .map(Ban::getEndTime)
                .filter(endTime -> Instant.now().isBefore(endTime))
                .max(Instant::compareTo)
                .orElse(null);

        LoggedInUser loggedInUser = LoggedInUser.builder()
                .username(user.getUsername())
                .id(user.getId())
                .isAdmin(user.getAdmin())
                .banDuration(longestCurrentBan)
                .build();

        System.out.println(loggedInUser);
        return loggedInUser;
    }

}
