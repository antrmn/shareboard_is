package usecase.comment;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;

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
        value={NewCommentServlet.class},
        cdiStereotypes = CdiMock.class)
@Disabled
public class NewCommentServletTest extends ServletTest {

    @Mock private CommentService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject NewCommentServlet commentServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("parent")).thenReturn("0");
        when(request.getParameter("text")).thenReturn("text");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        NewCommentServlet spyServlet = Mockito.spy(commentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        commentServlet.doPost(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("parent")).thenReturn("0");
        when(request.getParameter("text")).thenReturn("text");

        NewCommentServlet spyServlet = Mockito.spy(commentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        doThrow(ConstraintViolationException.class).when(service).newComment(anyString(),anyInt());
        doThrow(ConstraintViolationException.class).when(service).newCommentReply(anyString(), anyInt());
        assertThrows(ConstraintViolationException.class,() -> commentServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("parent")).thenReturn("0");
        when(request.getParameter("text")).thenReturn("text");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        NewCommentServlet spyServlet = Mockito.spy(commentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        commentServlet.doGet(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("parent")).thenReturn("0");
        when(request.getParameter("text")).thenReturn("text");

        NewCommentServlet spyServlet = Mockito.spy(commentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        doThrow(ConstraintViolationException.class).when(service).newComment(anyString(),anyInt());
        doThrow(ConstraintViolationException.class).when(service).newCommentReply(anyString(), anyInt());
        assertThrows(ConstraintViolationException.class,() -> commentServlet.doGet(request,response));
    }


}