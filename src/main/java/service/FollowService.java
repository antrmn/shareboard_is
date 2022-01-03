package service;

import persistence.model.Follow;
import persistence.model.User;
import persistence.repo.FollowRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
public class FollowService {
    @Inject private SectionRepository sectionRepo;
    @Inject private FollowRepository followRepo;
    @Inject private UserRepository userRepo;
    @Resource private EJBContext ctx;
    private User user;

    public FollowService(){
        user = userRepo.getByName(ctx.getCallerPrincipal().getName());
    }

    @Transactional
    public void follow(int sectionId){
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
