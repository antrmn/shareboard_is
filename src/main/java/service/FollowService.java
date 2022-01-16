package service;

import persistence.model.Follow;
import persistence.model.User;
import persistence.repo.FollowRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import service.dto.LoggedInUser;
import service.validation.SectionExists;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
public class FollowService {
    @Inject private SectionRepository sectionRepo;
    @Inject private FollowRepository followRepo;
    @Inject private UserRepository userRepo;
    @Resource private LoggedInUser loggedInUser;

    @Transactional
    @RolesAllowed({"user","admin"})
    public void follow(@SectionExists int sectionId){
        User user = userRepo.getByName(loggedInUser.getUsername());
        Follow.Id id = new Follow.Id();
        id.setUser(user);
        id.setSection(sectionRepo.findById(sectionId));
        Follow follow = new Follow();
        follow.setId(id);
        followRepo.insert(follow);
    }

    @Transactional
    @RolesAllowed({"user","admin"})
    public void unFollow(@SectionExists int sectionId){
        Follow.Id id = new Follow.Id();
        id.setUser(userRepo.getByName(loggedInUser.getUsername()));
        id.setSection(sectionRepo.findById(sectionId));
        Follow follow = followRepo.findById(id);
        followRepo.remove(follow);
    }

}
