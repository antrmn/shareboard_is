package http.controller.interceptor;

import http.util.interceptor.InterceptableServlet;
import http.util.interceptor.ServletInterceptorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ForwardOnErrorInterceptorTest {

    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock RequestDispatcher requestDispatcher;

    @BeforeAll
    void register(){
        try{
            ServletInterceptorFactory.register(ForwardOnErrorInterceptor.class);
        }catch(IllegalArgumentException e){
            // già registrato? va bene lo stesso
        }
    }


    @Test
    void IllegalArgumentTest() throws ServletException, IOException {
        when(req.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("GET");
        servlet.service(req,resp);
        verify(req).setAttribute("errors", List.of("test"));
        verify(resp).setStatus(400);
        verify(requestDispatcher).forward(req, resp);
    }

    @Test
    void IllegalArgumentWithNullMessageTest() throws ServletException, IOException {
        when(req.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("HEAD");
        servlet.service(req,resp);
        verify(req).setAttribute("errors", List.of());
        verify(resp).setStatus(400);
        verify(requestDispatcher).forward(req, resp);
    }

    @Test
    void ConstraintViolationTest() throws ServletException, IOException {
        when(req.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("POST");
        servlet.service(req,resp);
        verify(req).setAttribute(any(), any());
        verify(resp).setStatus(400);
        verify(requestDispatcher).forward(req, resp);
    }

    @Test
    void NoErrorTest() throws ServletException, IOException {
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("PUT");
        servlet.service(req,resp);

        verify(req, never()).setAttribute(any(), any());
        verify(resp, never()).setStatus(400);
        verify(requestDispatcher, never()).forward(req, resp);
    }

    @ForwardOnError("page")
    private static class SampleInterceptableServlet extends InterceptableServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            throw new IllegalArgumentException("test");
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Set<ConstraintViolation<String>> collect = List.of("test1", "test2", "test3").stream().map((string) -> {
                ConstraintViolation<String> cv = (ConstraintViolation<String>) Mockito.mock(ConstraintViolation.class);
                when(cv.getMessage()).thenReturn(string);
                return cv;
            }).collect(Collectors.toSet());

            throw new ConstraintViolationException(collect);
        }

        @Override
        protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            throw new IllegalArgumentException((String) null);
        }

        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }
    }
}