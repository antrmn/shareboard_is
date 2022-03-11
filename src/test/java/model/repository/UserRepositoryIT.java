package model.repository;

import model.entity.Ban;
import model.entity.User;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Classes(cdi = true, value = {UserRepository.class, GenericRepository.class})
public class UserRepositoryIT extends PersistenceIT{

    @Inject private UserRepository userRepository;
    @Inject private GenericRepository genericRepository;

    private List<User> users;

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
        });
    }

    @Test
    public void testRetrieve() throws Exception {
        doThenRollback(() -> {
            User user = genericRepository.findByNaturalId(User.class,users.get(0).getUsername(),true);
            Assertions.assertEquals(user.getId(),users.get(0).getId());
        });

        doThenRollback(() -> {
            User user = userRepository.getByEmail(users.get(0).getEmail());
            Assertions.assertEquals(user.getId(),users.get(0).getId());
        });

        doThenRollback(() -> {
            User user = genericRepository.findByNaturalId(User.class,users.get(0).getUsername(), true);
            Assertions.assertEquals(user.getId(),users.get(0).getId());
            Assertions.assertEquals(user.getUsername(),users.get(0).getUsername());
        });

    }

    @Test
    public void testBan() throws Exception {
        Ban ban = new Ban();
        ban.setUser(users.get(0));
        Instant endTime = Instant.now().plus(2,ChronoUnit.DAYS);
        ban.setEndTime(endTime);




        doThenRollback((em) -> {
            genericRepository.insert(ban);
            Integer banId = ban.getId();

            User user = genericRepository.findById(User.class,users.get(0).getId());
            Ban foundBan = user.getBans().stream().filter(x -> x.getId().equals(banId)).findAny().orElse(null);
            Assertions.assertNotNull(foundBan);
            Assertions.assertEquals(foundBan.getEndTime(), endTime);
        });
    }

}
