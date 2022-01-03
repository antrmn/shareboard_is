package service;

import persistence.model.Follow;
import persistence.model.User;
import persistence.repo.FollowRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import service.dto.LoggedInUser;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
public class FollowService {
    @Inject private SectionRepository sectionRepo;
    @Inject private FollowRepository followRepo;
    @Inject private UserRepository userRepo;
    @Inject private LoggedInUser loggedInUser;


    @Transactional
    @RolesAllowed({"user","admin"})
    public void follow(int sectionId){
        User user = userRepo.getByName(loggedInUser.getUsername());
        Follow.Id id = new Follow.Id();
        id.setUser(user);
        id.setSection(sectionRepo.findById(sectionId));

        Follow follow = new Follow();
        follow.setId(id);
        followRepo.insert(follow);
    }

    @Transactional
    public void unFollow(int sectionId){
        Follow follow = followRepo.getBySection(sectionRepo.findById(sectionId));
        followRepo.remove(follow);
    }

}
