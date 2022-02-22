package persistence.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void getCommentCount(){
        Post post = new Post();
        post.comments = List.of(new Comment());
        assertEquals(post.getCommentCount(),1);
        Post postWithNoComments = new Post();
        postWithNoComments.comments = null;
        assertEquals(postWithNoComments.getCommentCount(),0);
    }

    @Test
    void getVote() {
        Map<User,PostVote> pvs = new HashMap<>();

        Post post = new Post();
        post.votes = pvs;
        post.id =5;

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        User user3 = new User();
        user3.setId(3);


        pvs.put(user1,new PostVote(user1,post,(short)+1));
        pvs.put(user2,new PostVote(user2,post,(short)-1));


        assertNotNull(post.getVote(user1));
        assertNotNull(post.getVote(user2));
        assertNull(post.getVote(user3));

    }

    @Test
    void testEquals() {
        Post post1 = new Post();
        post1.setId(1);

        Post post2 = new Post();
        post2.setId(1);

        Post post3 = new Post();
        post3.setId(2);

        assertEquals(post1,post1);
        assertEquals(post1,post2);
        assertNotEquals(post1,new Object());
        assertNotEquals(post1,post3);

        Post postNullId = new Post();
        assertNotEquals(postNullId,post1);
    }

    @Test
    void testHashCode() {
        Post post1 = new Post();
        post1.setId(1);

        Post post2 = new Post();
        post2.setId(1);

        Post post3 = new Post();
        post3.setId(2);

        assertEquals(post1.hashCode(),post2.hashCode());
        assertEquals(post2.hashCode(),post3.hashCode());
    }
}