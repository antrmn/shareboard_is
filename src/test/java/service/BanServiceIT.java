package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.*;
import service.dto.BanDTO;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Classes(cdi = true,
        value={BanService.class,
                BinaryContentRepository.class,
                CommentRepository.class,
                GenericRepository.class,
                PostRepository.class,
                SectionRepository.class,
                UserRepository.class},
        cdiInterceptors = BValInterceptor.class)
public class BanServiceIT extends PersistenceIT {
    @Inject private BanService service;
    @Inject private GenericRepository repository;

    private List<User> users;

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            users = IntStream.range(1, 4).mapToObj(n -> {
                User user = new User();
                user.setUsername("user" + n);
                user.setEmail("user" + n + "@email.it");
                user.setDescription("Description for user " + n);
                user.setAdmin(n % 2 == 0);
                user.setPicture("picture" + n);
                user.setPassword((n + "password").getBytes(StandardCharsets.UTF_8));
                user.setSalt((n + "salt").getBytes(StandardCharsets.UTF_8));
                return user;
            }).map(repository::insert).collect(Collectors.toList());
        });
    }

    @Test
    void successfulAddBanWithFutureDate() throws Exception {
        doThenRollback(() -> {
            int year = LocalDate.now().getYear()+1;
            Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);

            Ban ban2 = service.addBan(data,users.get(0).getId());
            List<BanDTO> bans = service.retrieveUserBan(users.get(0).getId());
            assertEquals(bans.get(0).getBanId(),ban2.getId());
        });
    }

}