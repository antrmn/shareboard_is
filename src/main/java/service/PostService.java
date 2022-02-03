package service;

import persistence.model.Post;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.*;
import service.auth.AuthenticationRequired;
import service.auth.DenyBannedUsers;
import service.dto.*;
import service.validation.Image;
import service.validation.PostExists;
import service.validation.SectionExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static persistence.model.Post.Type.IMG;
import static persistence.model.Post.Type.TEXT;
import static service.dto.PostSearchForm.SortCriteria.NEWEST;


@ApplicationScoped
@Transactional
public class PostService {
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private SectionRepository sectionRepo;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private FollowRepository followRepo;
    @Inject private CurrentUser currentUser;

    @Inject private EntityManager em;

    private List<Post> getPosts(){
        return em.createQuery("from Post p", Post.class).getResultList();
    }

    private List<Post> getPosts(User user){
        return em.createQuery("from Post p left join fetch PostVote pv on pv.post = p and pv.user = :user",
                Post.class).setParameter("user", user).getResultList();
    }

    public void myTest(){
        em.clear();
        User byId = userRepo.findById(19);
        List<Post> posts = getPosts();


        for(Post post : posts){
            post.getVote(byId);
        }

        em.clear();
        User byId2 = userRepo.findById(19);
        List<Post> posts2 = getPosts(byId2);

        for(Post post : posts2){
            post.getVote(byId);
        }
    }



    private PostPage mapPost(Post post){
        return PostPage.builder()
                .id(post.getId())
                .title(post.getTitle())
                .vote(1) //todo
                .votes(post.getVotes())
                .sectionName(post.getSection().getName())
                .authorName(post.getAuthor().getUsername())
                .sectionId(post.getSection().getId())
                .authorId(post.getAuthor().getId())
                .content(post.getContent())
                .nComments(100) //todo
                .build();
    }

    public PostPage getPost(@PostExists int id){
        Post p = postRepo.findById(id);
        // TODO: voto personale e n commenti
        PostPage post = mapPost(p);
        return post;
    }

    public List<PostPage> loadPosts(PostSearchForm form){
        final int elementsPerPage = 10;
        PostRepository.PostFinder finder = postRepo.getFinder();

        finder.limit(elementsPerPage);

        int page = form.getPage() > 0 ? form.getPage() : 1;
        finder.offset((page == 1) ? 0 : (page-1) * elementsPerPage+1);

        if(form.getContent() != null) {
            finder.byContent(form.getContent());
        }
        if(form.isIncludeBody()){
            finder.includeBody();
        }
        if(form.getSectionName() != null){
            Section section = sectionRepo.getByName(form.getSectionName());
            if(section != null){
                finder.bySection(section);
            }
        }
        if(form.getAuthorName() != null){
            User user = userRepo.getByName(form.getAuthorName());
            if(user != null){
                finder.byAuthor(user);
            }
        }
        if(form.isOnlyFollow() && currentUser.isLoggedIn()){
            User user = userRepo.getByName(currentUser.getUsername());
            if(user != null) {
                List<Section> collect = followRepo.getByUser(user).stream()
                        .map(x -> x.getId().getSection())
                        .collect(Collectors.toList());
                finder.bySections(collect);
            }
        }
        if(form.getPostedAfter() != null){
            finder.postedAfter(form.getPostedAfter());
        }
        if(form.getPostedBefore() != null){
            finder.postedBefore(form.getPostedBefore());
        }

        //null-safe switch
        switch(form.getOrderBy() != null ? form.getOrderBy() : NEWEST){
            case NEWEST: default: {
                finder.getNewest();
                break;
            }
            case OLDEST:{
                finder.getOldest();
                break;
            }
            case MOSTVOTED:{
                finder.getMostVoted();
                break;
            }
        }
        return finder.getResults().stream().map(this::mapPost).collect(Collectors.toList());
    }

    @AuthenticationRequired //TODO: check autore post?
    @DenyBannedUsers
    public void delete(@PostExists int id){
        postRepo.remove(postRepo.findById(id));
    }

    @AuthenticationRequired
    @DenyBannedUsers
    public void editPost(PostEditDTO edit, @PostExists int id){
        Post post = postRepo.findById(id);
        post.setTitle(edit.getTitle());
        post.setContent(edit.getContent());
        post.setType(edit.getType());
    }

    @AuthenticationRequired
    public void editPost(PostEditDTO edit,
                         @PostExists int id,
                         @Image BufferedInputStream content){
        Post post = postRepo.findById(id);
        post.setTitle(edit.getTitle());
        String fileName;
        try {
            fileName = bcRepo.insert(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        post.setContent(fileName);
        post.setType(edit.getType());
    }


    @AuthenticationRequired
    @DenyBannedUsers
    public int newPost(@NotBlank(message="{post.title.blank}") String title,
                       String body,
                       @SectionExists String sectionName){
        User user = userRepo.getByName(currentUser.getUsername());
        Section section = sectionRepo.getByName(sectionName);


        Post post = new Post();
        post.setTitle(title);
        post.setContent(body == null || body.isBlank() ? "" : body);
        post.setAuthor(user);
        post.setSection(section);
        post.setType(TEXT);

        return postRepo.insert(post).getId();
    }

    @AuthenticationRequired
    @DenyBannedUsers
    public int newPost(@NotBlank(message = "{post.title.blank}") String title,
                       @Image BufferedInputStream content,
                       long size, //todo range
                       @SectionExists String sectionName){
        User user = userRepo.getByName(currentUser.getUsername());
        Section section = sectionRepo.getByName(sectionName);

        String fileName;
        try {
            fileName = bcRepo.insert(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Post post = new Post();
        post.setTitle(title);
        post.setAuthor(user);
        post.setContent(fileName);
        post.setSection(section);
        post.setType(IMG);

        return postRepo.insert(post).getId(); //in caso di errore il file resta
    }
}
