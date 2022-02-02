package service;

import persistence.model.Follow;
import persistence.model.User;
import persistence.repo.FollowRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import service.auth.AuthenticationRequired;
import service.dto.CurrentUser;
import service.validation.SectionExistsById;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FollowService {
    @Inject private SectionRepository sectionRepo;
    @Inject private FollowRepository followRepo;
    @Inject private UserRepository userRepo;
    @Inject private CurrentUser currentUser;

    @AuthenticationRequired
    public Follow follow(@SectionExistsById int sectionId){
        User user = userRepo.getByName(currentUser.getUsername());
        Follow.Id id = new Follow.Id();
        id.setUser(user);
        id.setSection(sectionRepo.findById(sectionId));
        Follow follow = new Follow();
        follow.setId(id);
        return followRepo.insert(follow);
    }

    @AuthenticationRequired
    public void unFollow(@SectionExistsById int sectionId){
        Follow.Id id = new Follow.Id();
        id.setUser(userRepo.getByName(currentUser.getUsername()));
        id.setSection(sectionRepo.findById(sectionId));
        Follow follow = followRepo.findById(id);
        followRepo.remove(follow);
    }

}
