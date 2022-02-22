package persistence.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostVoteTest {

    @Test
    void testEquals() {
        User user1 = new User();
        user1.setId(1);
        Post post1 = new Post();
        post1.setId(1);
        PostVote postVote1 = new PostVote(user1,post1, (short) -1);

        User user2 = new User();
        user2.setId(2);
        Post post2 = new Post();
        post2.setId(2);
        PostVote postVote2 = new PostVote(user2,post2, (short) -1);
        PostVote postVote3 = new PostVote(user1,post2, (short) -1);
        PostVote postVote4 = new PostVote();
        postVote4.setUser(user2);
        postVote4.setPost(post1);
        postVote4.setVote((short) -1);
        PostVote postVote5 = new PostVote(user1,post1, (short) -1);

        List<Object> objs = List.of(postVote1,postVote2,postVote3,postVote4, postVote5, new Object());
        List<Object> ids = List.of(postVote1.getId(),postVote2.getId(),postVote3.getId(),postVote4.getId(), postVote5.getId(), new Object());

        Map<PostVote, Integer> map = Map.of(postVote1, 2, postVote2, 1, postVote3, 1, postVote4, 1);
        map.forEach((k,v) -> assertEquals(objs.stream().filter(k::equals).count(), (long)map.get(k)));


        map.forEach((k,v) -> assertEquals(ids.stream().filter(x -> k.getId().equals(x)).count(), (long)map.get(k)));
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        user1.setId(1);
        Post post1 = new Post();
        post1.setId(1);
        PostVote postVote1 = new PostVote(user1,post1, (short) -1);

        User user2 = new User();
        user2.setId(2);
        Post post2 = new Post();
        post2.setId(2);
        PostVote postVote2 = new PostVote(user2,post2, (short) -1);
        PostVote postVote3 = new PostVote(user1,post2, (short) -1);
        PostVote postVote4 = new PostVote(user2,post1, (short) -1);
        PostVote postVote5 = new PostVote(user1,post1, (short) -1);

        assertEquals(postVote1,postVote5 );
        assertEquals(postVote1.getId(),postVote5.getId() );
    }
}