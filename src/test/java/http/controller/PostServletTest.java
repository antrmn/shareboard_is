package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.CommentService;
import service.PostService;;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={PostServlet.class},
        cdiStereotypes = CdiMock.class)
public class PostServletTest extends ServletTest{

    @Mock private CommentService commentService;
    @Mock private PostService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject PostServlet postServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{

        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("comment")).thenReturn("0");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        PostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(service).getPost(anyInt());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("comment")).thenReturn("0");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        PostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(service).getPost(anyInt());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doGet(request,response));
    }


}