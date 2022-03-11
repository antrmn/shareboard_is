package model.repository;

import model.entity.Comment;
import model.entity.Post;
import model.entity.Section;
import model.entity.User;
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

@Classes(cdi = true, value = {CommentRepository.class, GenericRepository.class})
public class CommentRepositoryIT extends PersistenceIT{

    @Inject private CommentRepository commentRepository;
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
    public void testRetrieveByPost() throws Exception {
        Post post = posts.get(0);
        List<Comment> commentByPosts = commentRepository.getByPost(post, 999);
        commentByPosts.forEach((comment) -> {
            Comment foundComment = comments.get(post.getId()).stream()
                    .filter(x -> x.getId().equals(comment.getId()))
                    .findFirst().orElse(null);

            Assertions.assertNotNull(foundComment);
            Assertions.assertEquals(comment.getId(),foundComment.getId());
            Assertions.assertEquals(comment.getPost().getId(),foundComment.getPost().getId());
            Assertions.assertTrue(comment.getParentComment() == null && foundComment.getParentComment() == null
                    || comment.getParentComment().getId().equals(foundComment.getParentComment().getId()));
        });
    }

    @Test
    public void testRetrieveReplies() throws Exception {
        Post post = posts.get(0);
        Comment parentComment = comments.get(post.getId()).get(1);
        List<Comment> commentByPosts = commentRepository.getReplies(parentComment, 999);
        commentByPosts.forEach((comment) -> {
            Comment foundComment = comments.get(post.getId()).stream()
                    .filter(x -> x.getId().equals(comment.getId()))
                    .findFirst().orElse(null);

            Assertions.assertNotNull(foundComment);
            Assertions.assertEquals(comment.getId(),foundComment.getId());
            Assertions.assertEquals(comment.getPost().getId(),foundComment.getPost().getId());
            Assertions.assertEquals(comment.getParentComment().getId(),foundComment.getParentComment().getId());
            Assertions.assertEquals(comment.getVotesCount(),foundComment.getVotesCount());
        });
    }
}
