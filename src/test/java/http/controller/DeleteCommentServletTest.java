package http.controller;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.Comment;
import persistence.model.Post;
import rocks.limburg.cdimock.CdiMock;
import service.CommentService;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@Classes(cdi = true,
        value={DeleteCommentServlet.class},
        cdiStereotypes = CdiMock.class)
public class DeleteCommentServletTest extends ServletTest{

    @Mock private CommentService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject private DeleteCommentServlet servlet;

    @Test
    void successfulDoGet() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        Comment comment = mock(Comment.class);
        comment.setId(3);
        Post post = new Post();
        post.setId(2);
        when(comment.getPost()).thenReturn(post);
        when(service.getComment(anyInt())).thenReturn(comment);

        DeleteCommentServlet spyServlet = spy(servlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doGet(request,response);
        verify(service, times(1)).delete(anyInt());
    }

    @Test
    void failDoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("-5");
        Comment comment = mock(Comment.class);
        comment.setId(3);
        Post post = new Post();
        post.setId(2);
        when(comment.getPost()).thenReturn(post);
        when(service.getComment(anyInt())).thenReturn(comment);

        doThrow(ConstraintViolationException.class).when(service).delete(anyInt());

        assertThrows(ConstraintViolationException.class,() ->servlet.doGet(request,response));
    }

    @Test
    void successfulDoPost() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        Comment comment = mock(Comment.class);
        comment.setId(3);
        Post post = new Post();
        post.setId(2);
        when(comment.getPost()).thenReturn(post);
        when(service.getComment(anyInt())).thenReturn(comment);

        DeleteCommentServlet spyServlet = spy(servlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doPost(request,response);
        verify(service, times(1)).delete(anyInt());
    }

    @Test
    void failDoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("-5");
        Comment comment = mock(Comment.class);
        comment.setId(3);
        Post post = new Post();
        post.setId(2);
        when(comment.getPost()).thenReturn(post);
        when(service.getComment(anyInt())).thenReturn(comment);

        doThrow(ConstraintViolationException.class).when(service).delete(anyInt());

        assertThrows(ConstraintViolationException.class,() ->servlet.doPost(request,response));
    }



}
