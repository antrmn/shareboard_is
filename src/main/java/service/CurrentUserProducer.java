package service;

import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.BanRepository;
import persistence.repo.UserRepository;
import service.dto.CurrentUser;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.security.Principal;
import java.time.Instant;

@ApplicationScoped
public class CurrentUserProducer {
    @Inject private Principal principal;
    @Inject UserRepository userRepo;
    @Inject BanRepository banRepo;

    @Named("currentUser")
    @RequestScoped
    @Produces
    public CurrentUser produceCurrentUser(){
        if(principal == null || principal.getName() == null)
            return new CurrentUser();

        User user = userRepo.getByName(principal.getName());
        if(user == null)
            return new CurrentUser();

        Instant longestCurrentBan = banRepo.getByUserCurrent(user).stream()
                .map(Ban::getEndTime)
                .filter(endTime -> Instant.now().isBefore(endTime))
                .max(Instant::compareTo)
                .orElse(null);

        CurrentUser currentUser = CurrentUser.builder()
                .username(user.getUsername())
                .id(user.getId())
                .isAdmin(user.getAdmin())
                .banDuration(longestCurrentBan)
                .isLoggedIn(true)
                .build();

        System.out.println(currentUser);
        return currentUser;
    }

}
