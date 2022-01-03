package service;

import persistence.model.Post;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.*;
import service.dto.LoggedInUser;
import service.validation.Image;
import service.validation.SectionExists;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.*;

import static persistence.model.Post.Type.*;


@Stateless
@Service
public class PostService {
    @Inject private PostRepository postRepo;
    @Inject private UserRepository userRepo;
    @Inject private SectionRepository sectionRepo;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private LoggedInUser loggedInUser;

    @RolesAllowed({"user","admin"})
    public int newPost(@NotBlank(message="{post.title.blank}") @Size() String title,
    @Size() String body,
    @SectionExists String sectionName){
        User user = userRepo.getByName(loggedInUser.getUsername());
        Section section = sectionRepo.getByName(sectionName);

        Post post = new Post();
        post.setTitle(title);
        post.setContent(body == null || body.isBlank() ? "" : body);
        post.setAuthor(user);
        post.setSection(section);
        post.setType(TEXT);

        return postRepo.insert(post).getId();
    }

    @RolesAllowed({"user","admin"})
    public int newPost(@NotBlank(message = "{post.title.blank}") String title,
                       @Image BufferedInputStream content,
                       long size, //todo range
                       @SectionExists String sectionName){
        User user = userRepo.getByName(loggedInUser.getUsername());
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
