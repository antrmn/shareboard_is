package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.Follow;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.*;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@Classes(cdi = true,
        value={FollowService.class,
                BinaryContentRepository.class,
                CommentRepository.class,
                GenericRepository.class,
                PostRepository.class,
                SectionRepository.class,
                UserRepository.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class FollowServiceIT extends PersistenceIT {
    @Mock private CurrentUser currentUser;
    @Inject private GenericRepository repository;
    @Inject private FollowService followService;

    private List<User> users;
    private Section section;

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

            section = new Section();
            section.setName("Nome sezione");
            section.setDescription("Description");
            section.setPicture("picture");
            section.setBanner("banner");
            repository.insert(section);

        });
    }

    @Test
    void successfullyFollow() throws Exception {
        doThenRollback((em) -> {
            when(currentUser.getId()).thenReturn(users.get(0).getId());

            followService.follow(section.getId());
            em.flush();
            em.clear();
            Section section2 = repository.findById(Section.class,section.getId());
            Follow follow1 = section2.getFollow(users.get(0));
            assertNotNull(follow1);
        });
    }

    @Test
    void successfullyUnfollow() throws Exception {
        doThenRollback((em) -> {

            when(currentUser.getId()).thenReturn(users.get(0).getId());

            followService.follow(section.getId());
            em.flush();

            Section section2 = repository.findById(Section.class,section.getId());

            followService.unFollow(section.getId());
            em.flush();

            Follow follow1 = section2.getFollow(users.get(0));
            assertTrue(follow1 == null);
        });
    }

}
