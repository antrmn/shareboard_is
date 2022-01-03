package service;

import persistence.model.Post;
import persistence.repo.BanRepository;
import persistence.repo.PostRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import service.dto.PostPage;

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
    public int newPost(NewPostDTO postData){
        Post newPost = new Post();
        return postRepo.insert(newPost).getId();

    }

    public PostPage GetPost(int id){
        Post p = postRepo.findById(id);
        PostPage post = new PostPage();
    }






}
