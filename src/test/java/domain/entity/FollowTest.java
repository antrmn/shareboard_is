package domain.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FollowTest {

    @Test
    void testEquals() {
        User user1 = new User();
        user1.setId(1);
        Section section1 = new Section();
        section1.setId(1);
        Follow follow1 = new Follow(user1,section1);

        User user2 = new User();
        user2.setId(2);
        Section section2 = new Section();
        section2.setId(2);
        Follow follow2 = new Follow(user2,section2);
        Follow follow3 = new Follow(user1,section2);
        Follow follow4 = new Follow();
        follow4.setUser(user2);
        follow4.setSection(section1);
        Follow follow5 = new Follow(user1,section1);

        List<Object> objs = List.of(follow1,follow2,follow3,follow4, follow5, new Object());
        List<Object> ids = List.of(follow1.getId(),follow2.getId(),follow3.getId(),follow4.getId(), follow5.getId(), new Object());

        Map<Follow, Integer> map = Map.of(follow1, 2, follow2, 1, follow3, 1, follow4, 1);
        map.forEach((k,v) -> assertEquals(objs.stream().filter(k::equals).count(), (long)map.get(k)));


        map.forEach((k,v) -> assertEquals(ids.stream().filter(x -> k.getId().equals(x)).count(), (long)map.get(k)));
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        user1.setId(1);
        Section section1 = new Section();
        section1.setId(1);
        Follow follow1 = new Follow(user1,section1);

        User user2 = new User();
        user2.setId(2);
        Section section2 = new Section();
        section2.setId(2);
        Follow follow2 = new Follow(user2,section2);
        Follow follow3 = new Follow(user1,section2);
        Follow follow4 = new Follow(user2,section1);
        Follow follow5 = new Follow(user1,section1);

        assertEquals(follow1,follow5 );
        assertEquals(follow1.getId(),follow5.getId() );
    }
}