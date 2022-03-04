package usecase;

import org.apache.openejb.junit5.ExtensionMode;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import rocks.limburg.cdimock.CdiMocking;

import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith({MockitoExtension.class, CdiMocking.class})
@MockitoSettings(strictness = LENIENT)
@RunWithApplicationComposer(mode = ExtensionMode.PER_ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
public abstract class ServletTest {
}
