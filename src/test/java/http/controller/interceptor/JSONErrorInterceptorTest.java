package http.controller.interceptor;

import http.util.interceptor.InterceptableServlet;
import http.util.interceptor.ServletInterceptorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import service.auth.AuthenticationRequiredException;
import service.auth.BannedUserException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class JSONErrorInterceptorTest{

    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;

    @BeforeAll
    void register(){
        try{
            ServletInterceptorFactory.register(JSONErrorInterceptor.class);
        }catch(IllegalArgumentException e){
            // già registrato? va bene lo stesso
        }
    }

    private static List<Exception> provideExceptions(){
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
                new AuthenticationRequiredException(),
                new RuntimeException(),
                new ServletException(),
                new IOException()
        );
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void testExceptions(Exception ex) throws ServletException, IOException {
        when(req.getServletContext()).thenReturn(mock(ServletContext.class));
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("GET");

        PrintWriter print = mock(PrintWriter.class);
        when(resp.getWriter()).thenReturn(print);

        when(req.getAttribute(any())).thenReturn(ex);
        servlet.service(req,resp);

        verify(resp).setStatus(anyInt());
        verify(print).print(anyString());

    }

    @JSONError
    private static class SampleInterceptableServlet extends InterceptableServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Exception exception = (Exception) req.getAttribute("ex");
            if(exception instanceof RuntimeException){
                throw (RuntimeException)exception;
            } else if (exception instanceof ServletException){
                throw (ServletException)exception;
            } else if(exception instanceof IOException){
                throw (IOException)exception;
            }
        }

    }
}