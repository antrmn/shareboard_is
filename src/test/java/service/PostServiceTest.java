package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.ExtensionMode;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.PostRepository;
import persistence.repo.SectionRepository;
import persistence.repo.UserRepository;
import rocks.limburg.cdimock.CdiMock;
import rocks.limburg.cdimock.CdiMocking;
import security.Pbkdf2PasswordHashImpl;
import service.dto.UserIdentityDTO;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;


@Classes(cdi = true,
        value={PostService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class PostServiceTest extends ServiceTest{

    @Mock SectionRepository sectionRepository;
    @Mock PostRepository postRepository;
    @Mock UserRepository userRepository;
    @Mock BinaryContentRepository binaryContentRepository;
    @Inject PostService service;

    @BeforeEach
    void setUp() {}

    @AfterEach
    void tearDown() {}

    @Test
    public void noSectionFoundNewPost(){
        String sectionName = "idontexist";
        when(sectionRepository.getByName(sectionName)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,
                () -> service.newPost("title", "body", sectionName));
    }
}