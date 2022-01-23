package service;

import persistence.model.Post;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.PostRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import service.auth.AuthenticationRequired;
import service.auth.DenyBannedUsers;
import service.dto.*;
import service.validation.Image;
import service.validation.PostExists;
import service.validation.SectionExists;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static persistence.model.Post.Type.IMG;
import static persistence.model.Post.Type.TEXT;


@ApplicationScoped
@Transactional
public class PostService {
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private SectionRepository sectionRepo;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private CurrentUser currentUser;


    public PostPage getPost(@PostExists int id){
        Post p = postRepo.findById(id);
        UserLite user = new UserLite(p.getAuthor().getId(), p.getAuthor().getUsername());
        SectionLite section = new SectionLite(p.getSection().getId(), p.getSection().getName());
        // TODO: voto personale e n commenti
        PostPage post = new PostPage(p.getId(), p.getTitle(), p.getVotes(), 0, section, user, p.getContent(), 0);
        return post;
    }

    @AuthenticationRequired //TODO: check autore post?
    @DenyBannedUsers
    public void delete(@PostExists int id){
        postRepo.remove(postRepo.findById(id));
    }

    public List<PostPreview> fetchPosts(String text){
        List<Post> posts = postRepo.getFinder().byContent(text).getResults();
        List<PostPreview> previews = new ArrayList<>();
        for(Post p : posts){
            SectionPostPreview section = new SectionPostPreview(p.getSection().getName());
            UserPostPreview author = new UserPostPreview(p.getAuthor().getUsername());
            // TODO: voto personale e n commenti
            PostPreview preview = new PostPreview(p.getId(), p.getTitle(), 0, p.getVotes(), p.getType(), p.getContent(),
                                                    p.getCreationDate(), 0, section, author);
            previews.add(preview);
        }
        return previews;
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
