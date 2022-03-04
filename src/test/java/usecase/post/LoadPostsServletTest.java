package usecase.post;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={LoadPostsServlet.class},
        cdiStereotypes = CdiMock.class)
public class LoadPostsServletTest extends ServletTest {

    @Mock private PostService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject LoadPostsServlet servlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{

        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("usecase/section")).thenReturn("usecase/section");
        when(request.getParameter("author")).thenReturn("usecase/user");
        when(request.getParameter("postedAfter")).thenReturn("2021-01-01");
        when(request.getParameter("postedBefore")).thenReturn("2021-01-01");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getParameter("includeBody")).thenReturn("yes");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        servlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("usecase/section")).thenReturn("usecase/section");
        when(request.getParameter("author")).thenReturn("usecase/user");
        when(request.getParameter("postedAfter")).thenReturn("date");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(service).loadPosts(any());
        assertThrows(ConstraintViolationException.class,() -> servlet.doGet(request,response));
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{

        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("usecase/section")).thenReturn("usecase/section");
        when(request.getParameter("author")).thenReturn("usecase/user");
        when(request.getParameter("postedAfter")).thenReturn("date");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        servlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("usecase/section")).thenReturn("usecase/section");
        when(request.getParameter("author")).thenReturn("usecase/user");
        when(request.getParameter("postedAfter")).thenReturn("date");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(service).loadPosts(any());
        assertThrows(ConstraintViolationException.class,() -> servlet.doPost(request,response));
    }


}
