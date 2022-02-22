package persistence.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BanTest {

    @Test
    void testEquals() {
        User user1 = new User();
        Ban ban1 = new Ban();
        ban1.setUser(user1);
        ban1.setEndTime(Instant.now());
        ban1.setId(1);

        User user2 = new User();
        Ban ban2 = new Ban();
        ban2.setUser(user2);
        ban2.setEndTime(Instant.now());
        ban2.setId(1);

        User user3 = new User();
        Ban ban3 = new Ban();
        ban3.setUser(user3);
        ban3.setEndTime(Instant.now());
        ban3.setId(2);


        assertEquals(ban1,ban1);
        assertEquals(ban1,ban2);
        assertNotEquals(ban1,new Object());
        assertNotEquals(ban1,ban3);
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        Ban ban1 = new Ban();
        ban1.setUser(user1);
        ban1.setEndTime(Instant.now());
        ban1.setId(1);

        User user2 = new User();
        Ban ban2 = new Ban();
        ban2.setUser(user2);
        ban2.setEndTime(Instant.now());
        ban2.setId(1);

        User user3 = new User();
        Ban ban3 = new Ban();
        ban3.setUser(user3);
        ban3.setEndTime(Instant.now());
        ban3.setId(2);

        assertEquals(ban1.hashCode(),ban2.hashCode());
        assertEquals(ban2.hashCode(),ban3.hashCode());
    }
}