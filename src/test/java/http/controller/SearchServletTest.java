package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.PostService;

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
        value={SearchServlet.class},
        cdiStereotypes = CdiMock.class)
public class SearchServletTest extends ServletTest{

    @Mock private PostService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject SearchServlet searchServlet;

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("section")).thenReturn("section");
        when(request.getParameter("author")).thenReturn("user");
        when(request.getParameter("postedAfter")).thenReturn("date");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        searchServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).loadPosts(any());
        assertThrows(ConstraintViolationException.class,() -> searchServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("content")).thenReturn("content");
        when(request.getParameter("section")).thenReturn("section");
        when(request.getParameter("author")).thenReturn("user");
        when(request.getParameter("postedAfter")).thenReturn("date");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getParameter("orderby")).thenReturn("newest");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        searchServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).loadPosts(any());
        assertThrows(ConstraintViolationException.class,() -> searchServlet.doGet(request,response));
    }


}