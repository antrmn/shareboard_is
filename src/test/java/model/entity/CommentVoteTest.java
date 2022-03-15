package model.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentVoteTest {


    @Test
    void testEquals() {
        User user1 = new User();
        user1.setId(1);
        Comment comment1 = new Comment();
        comment1.setId(1);
        CommentVote commentVote1 = new CommentVote(user1,comment1, (short) -1);

        User user2 = new User();
        user2.setId(2);
        Comment comment2 = new Comment();
        comment2.setId(2);
        CommentVote commentVote2 = new CommentVote(user2,comment2, (short) -1);
        CommentVote commentVote3 = new CommentVote(user1,comment2, (short) -1);
        CommentVote commentVote4 = new CommentVote();
        commentVote4.setUser(user2);
        commentVote4.setComment(comment1);
        commentVote4.setVote((short) -1);
        CommentVote commentVote5 = new CommentVote(user1,comment1, (short) -1);

        List<Object> objs = List.of(commentVote1,commentVote2,commentVote3,commentVote4, commentVote5, new Object());
        List<Object> ids = List.of(commentVote1.getId(),commentVote2.getId(),commentVote3.getId(),commentVote4.getId(), commentVote5.getId(), new Object());

        Map<CommentVote, Integer> map = Map.of(commentVote1, 2, commentVote2, 1, commentVote3, 1, commentVote4, 1);
        map.forEach((k,v) -> assertEquals(objs.stream().filter(k::equals).count(), (long)map.get(k)));


        map.forEach((k,v) -> assertEquals(ids.stream().filter(x -> k.getId().equals(x)).count(), (long)map.get(k)));
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        user1.setId(1);
        Comment comment1 = new Comment();
        comment1.setId(1);
        CommentVote commentVote1 = new CommentVote(user1,comment1, (short) -1);

        User user2 = new User();
        user2.setId(2);
        Comment comment2 = new Comment();
        comment2.setId(2);
        CommentVote commentVote2 = new CommentVote(user2,comment2, (short) -1);
        CommentVote commentVote3 = new CommentVote(user1,comment2, (short) -1);
        CommentVote commentVote4 = new CommentVote(user2,comment1, (short) -1);
        CommentVote commentVote5 = new CommentVote(user1,comment1, (short) -1);

        assertEquals(commentVote1,commentVote5 );
        assertEquals(commentVote1.getId(),commentVote5.getId() );
    }
}