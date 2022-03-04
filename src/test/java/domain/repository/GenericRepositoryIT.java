package domain.repository;

import domain.entity.Comment;
import domain.entity.Post;
import domain.entity.Section;
import domain.entity.User;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Classes(cdi = true, value = {GenericRepository.class})
public class GenericRepositoryIT extends PersistenceIT{
    @Inject private GenericRepository genericRepository;
    private List<User> users;
    private List<Post> posts;
    private List<Section> sections;
    private Map<Integer,List<Comment>> comments = new HashMap<>();

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            users = IntStream.range(1, 4).mapToObj(n -> {
                User user = new User();
                user.setUsername("usecase/user" + n);
                user.setEmail("usecase/user" + n + "@email.it");
                user.setDescription("Description for user " + n);
                user.setAdmin(n % 2 == 0);
                user.setPicture("picture" + n);
                user.setPassword((n + "password").getBytes(StandardCharsets.UTF_8));
                user.setSalt((n + "salt").getBytes(StandardCharsets.UTF_8));
                return user;
            }).map(genericRepository::insert).collect(Collectors.toList());

            sections = IntStream.range(1,4).mapToObj(n -> {
                Section section = new Section();
                section.setName("usecase/section" +n);
                section.setPicture("picture" + n);
                section.setBanner("banner"+n);
                return section;
            }).map(genericRepository::insert).collect(Collectors.toList());

            posts = IntStream.range(1,10).mapToObj(n -> {
                Post post = new Post();
                post.setContent("content"+n);
                post.setTitle("title"+n);
                post.setType(n % 2 == 0 ? Post.Type.TEXT : Post.Type.IMG);
                post.setAuthor( users.get( n % users.size() ) );
                post.setSection( sections.get(n % sections.size()) );
                return post;
            }).map(genericRepository::insert).collect(Collectors.toList());

            IntStream.range(1,30).forEach(n -> {
                Comment comment = new Comment();
                comment.setContent("content" + n);
                comment.setPost( posts.get(n % posts.size() ));
                List<Comment> postComments = comments.computeIfAbsent(comment.getPost().getId(), ArrayList::new);

                if(postComments.size()<=3){
                    comment.setParentComment(null);
                } else {
                    comment.setParentComment(postComments.get(n % postComments.size()));
                }

                postComments.add(comment);
                genericRepository.insert(comment);
            });
        });
    }

    @Test
    public void findByIdTest() throws Exception {
        doThenRollback(() -> {
            Comment comment = comments.get( posts.get(0).getId() ).get(0);
            Comment commentFound = genericRepository.findById(Comment.class, comment.getId());
            Assertions.assertEquals(comment.getId(), commentFound.getId());
            Assertions.assertEquals(comment.getPost().getId(), posts.get(0).getId());
        });
    }

    @Test
    public void findAllTest() throws Exception {
        doThenRollback(() -> {
            List<Section> all = genericRepository.findAll(Section.class);
            Assertions.assertEquals(all.size(),sections.size());
        });
    }

    @Test
    public void countTest() throws Exception {
        doThenRollback(() -> {
            Long n = genericRepository.getCount(Section.class);
            Assertions.assertEquals(n,sections.size());
        });

        doThenRollback(() -> {
            Long n = genericRepository.getCount(Post.class);
            Assertions.assertEquals(n,posts.size());
        });
    }

    @Test
    public void mergeTest() throws Exception {
        doThenRollback(() -> {
            Post post = posts.get(0);
            post.setTitle("titolo cambiato");
            genericRepository.merge(post);

            Post editedPost = genericRepository.findById(Post.class, posts.get(0).getId(), true);
            Assertions.assertEquals("titolo cambiato", editedPost.getTitle());
        });

        //controlla se effettivamente c'è stato il rollback
        doThenRollback(() -> {
            Post byId = genericRepository.findById(Post.class, posts.get(0).getId());
            Assertions.assertNotEquals("titolo cambiato", byId.getTitle());
        });
    }

    @Test
    public void removeTest() throws Exception {
        Post postToremove = posts.get(3);
        doThenRollback(() -> {
            Post byId = genericRepository.findById(Post.class, postToremove.getId());
            genericRepository.remove(byId);

            Assertions.assertNull(genericRepository.findById(Post.class, postToremove.getId()));
        });

        Assertions.assertNotNull(genericRepository.findById(Post.class,postToremove.getId()));
    }
}
