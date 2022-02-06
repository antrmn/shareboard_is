package service;

import persistence.model.User;
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

    @Named("currentUser")
    @RequestScoped
    @Produces
    public CurrentUser produceCurrentUser(){
        if(principal == null || principal.getName() == null)
            return new CurrentUser();

        User user = userRepo.getByName(principal.getName());
        if(user == null)
            return new CurrentUser();

        Instant longestCurrentBan = user.getBans().isEmpty() ? null : user.getBans().get(0).getEndTime();

        CurrentUser currentUser = CurrentUser.builder()
                .username(user.getUsername())
                .id(user.getId())
                .isAdmin(user.getAdmin())
                .picture(user.getPicture())
                .banDuration(longestCurrentBan)
                .isLoggedIn(true)
                .build();

        System.out.println(currentUser);
        return currentUser;
    }

}
