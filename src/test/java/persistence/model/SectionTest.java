package persistence.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SectionTest {

    @Test
    void getFollow() {
        Section section = new Section();
        User user = new User();
        user.setId(1);
        section.setId(1);
        Follow follow = new Follow(user,section);
        section.follows = Map.of(user,follow);
        assertEquals(section.getFollow(user),follow);
    }

    @Test
    void getFollowCount() {
        Section section = new Section();
        User user = new User();
        user.setId(1);
        section.setId(1);
        Follow follow = new Follow(user,section);
        section.follows = Map.of(user,follow);
        assertEquals(section.getFollowCount(),1);
    }

    @Test
    void testEquals() {
        Section section1 = new Section();
        section1.setId(1);

        Section section2 = new Section();
        section2.setId(1);

        Section section3 = new Section();
        section3.setId(2);

        assertEquals(section1,section1);
        assertEquals(section1,section2);
        assertNotEquals(section1,new Object());
        assertNotEquals(section1,section3);
    }

    @Test
    void testHashCode() {
        Section section1 = new Section();
        section1.setId(1);

        Section section2 = new Section();
        section2.setId(1);

        Section section3 = new Section();
        section3.setId(2);

        assertEquals(section1.hashCode(),section2.hashCode());
        assertEquals(section2.hashCode(),section3.hashCode());
    }
}