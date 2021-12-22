package service;

import persistence.repo.BanRepository;
import persistence.repo.PostRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;


@Stateless
public class PostService {
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private SectionRepository sectionRepo;
    @Inject private BanRepository banRepo;
    @Resource private EJBContext ctx;

    @RolesAllowed({"user", "admin"})
    @Transactional
    public int newPost(String content, String sectionName, String title, String SectionName){
        return 1;
    }






}
