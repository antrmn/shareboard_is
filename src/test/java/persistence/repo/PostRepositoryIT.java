package persistence.repo;

import org.apache.openejb.testing.Classes;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import persistence.model.User;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@Classes(cdi = true, value = {PostRepository.class, GenericRepository.class})
public class PostRepositoryIT extends RepositoryIT{

    @Inject
    private PostRepository postRepository;
    @Inject
    private GenericRepository genericRepository;

    @Test
    public void hello() throws Exception {
        User user = new User();
        user.setAdmin(true);
        user.setUsername("utente");
        user.setPicture("picture");
        user.setSalt("ciao".getBytes(StandardCharsets.UTF_8));
        user.setPassword("ciao".getBytes(StandardCharsets.UTF_8));
        user.setEmail("aaaaaaaaaa@aaaaaaaaaaaa.aaaaaaaaaaaa");

        doTransactional((x) -> {
            genericRepository.insert(user);
        });

        AtomicReference<User> usera = new AtomicReference<>();
        doTransactional((x) -> {
            usera.set(genericRepository.findById(User.class, genericRepository.findById(User.class, usera.get().getId())));
        });

        Assertions.assertEquals(user.getId(), usera.get().getId());
        Assertions.assertEquals(user.getUsername(),usera.get().getUsername());
    }

}
