package model.entity;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void getVote() {
        Map<User,CommentVote> cvs = new HashMap<>();

        Comment comment = new Comment();
        comment.votes = cvs;
        comment.id =5;

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        User user3 = new User();
        user3.setId(3);


        cvs.put(user1,new CommentVote(user1,comment,(short)+1));
        cvs.put(user2,new CommentVote(user2,comment,(short)-1));


        assertNotNull(comment.getVote(user1));
        assertNotNull(comment.getVote(user2));
        assertNull(comment.getVote(user3));

    }

    @Test
    void testEquals() {
        Comment comment1 = new Comment();
        comment1.setId(1);

        Comment comment2 = new Comment();
        comment2.setId(1);

        Comment comment3 = new Comment();
        comment3.setId(2);

        assertEquals(comment1,comment1);
        assertEquals(comment1,comment2);
        assertNotEquals(comment1,new Object());
        assertNotEquals(comment1,comment3);
    }

    @Test
    void testHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1);

        Comment comment2 = new Comment();
        comment2.setId(1);

        Comment comment3 = new Comment();
        comment3.setId(2);

        assertEquals(comment1.hashCode(),comment2.hashCode());
        assertEquals(comment2.hashCode(),comment3.hashCode());
    }
}