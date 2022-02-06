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
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static persistence.model.Post.Type.IMG;
import static persistence.model.Post.Type.TEXT;
import static service.dto.PostSearchForm.SortCriteria.NEWEST;


@ApplicationScoped
@Transactional
public class PostService {

    @Inject private GenericRepository genericRepository;
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private SectionRepository sectionRepo;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private CurrentUser currentUser;


    private PostPage mapPost(Post post){
        User user = null;
        if(currentUser.isLoggedIn())
            user = genericRepository.findById(User.class,currentUser.getId());

        //converte persistence.Post.Type in service.dto.PostType (ew)
        PostType postType = post.getType() == IMG ? PostType.IMG : PostType.TEXT;

        return PostPage.builder()
                .id(post.getId())
                .title(post.getTitle())
                .vote(post.getVote(user) == null ? 0 : post.getVote(user).getVote())
                .votes(post.getVotesCount())
                .sectionName(post.getSection().getName())
                .authorName(post.getAuthor().getUsername())
                .sectionId(post.getSection().getId())
                .authorId(post.getAuthor().getId())
                .content(post.getContent())
                .creationDate(post.getCreationDate())
                .nComments(post.getCommentCount())
                .type(postType)
                .build();
    }

    public PostPage getPost(@PostExists int id){
        Post p = genericRepository.findById(Post.class,id);
        PostPage post = mapPost(p);
        return post;
    }

    @Transactional
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
            finder.joinUserFollows(genericRepository.findById(User.class,currentUser.getId()));
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
        genericRepository.remove(genericRepository.findById(User.class,id));
    }

    @AuthenticationRequired
    @DenyBannedUsers
    public void editPost(PostEditDTO edit, @PostExists int id){
        Post post = genericRepository.findById(Post.class, id);
        post.setTitle(edit.getTitle());
        post.setContent(edit.getContent());
        post.setType(edit.getType());
    }

    @AuthenticationRequired
    public void editPost(PostEditDTO edit,
                         @PostExists int id,
                         @Image BufferedInputStream content){
        Post post = genericRepository.findById(Post.class, id);
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

        return genericRepository.insert(post).getId();
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

        return genericRepository.insert(post).getId(); //in caso di errore il file resta
    }
}
