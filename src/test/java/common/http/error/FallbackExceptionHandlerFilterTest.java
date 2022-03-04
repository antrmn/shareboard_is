package common.http.error;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import usecase.auth.AuthenticationRequiredException;
import usecase.auth.BannedUserException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
class FallbackExceptionHandlerFilterTest {
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;


    private static List<RuntimeException> provideExceptions(){
        Set<ConstraintViolation<String>> cves = List.of("test1", "test2", "test3").stream().map((string) -> {
            ConstraintViolation<String> cv = (ConstraintViolation<String>) Mockito.mock(ConstraintViolation.class);
            when(cv.getMessage()).thenReturn(string);
            return cv;
        }).collect(Collectors.toSet());

        return List.of(
                new IllegalArgumentException("message"),
                new ConstraintViolationException(cves),
                new AuthenticationRequiredException("message"),
                new BannedUserException(),
                new BannedUserException(Instant.now()),
                new AuthenticationRequiredException()
        );
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void testExceptions(RuntimeException ex) throws ServletException, IOException {
        FilterChain chain = (req, resp) -> {throw ex;};

        FallbackExceptionHandlerFilter filter = new FallbackExceptionHandlerFilter();
        filter.doFilter(request,response,chain);
        verify(response).sendError(anyInt(),nullable(String.class));
    }

}