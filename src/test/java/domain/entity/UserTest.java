package domain.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void getBans() {
        User user = new User();
        Ban ban = new Ban();
        ban.setId(1);
        ban.setUser(user);
        user.bans = List.of(ban);
        assertEquals(user.bans,List.of(ban));
    }

    @Test
    void testEquals() {
        User user1 = new User();
        user1.setId(1);

        User user2 = new User();
        user2.setId(1);

        User user3 = new User();
        user3.setId(2);

        assertEquals(user1,user1);
        assertEquals(user1,user2);
        assertNotEquals(user1,new Object());
        assertNotEquals(user1,user3);
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        user1.setId(1);

        User user2 = new User();
        user2.setId(1);

        User user3 = new User();
        user3.setId(2);

        assertEquals(user1.hashCode(),user2.hashCode());
        assertEquals(user2.hashCode(),user3.hashCode());
    }
}