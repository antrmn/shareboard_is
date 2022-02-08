package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.CommentService;
import service.dto.CommentDTO;

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
        value={EditCommentServlet.class},
        cdiStereotypes = CdiMock.class)
public class EditCommentServletTest extends ServletTest{

    @Mock private CommentService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Mock private CommentDTO comment;
    @Inject EditCommentServlet editCommentServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{

        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("text")).thenReturn("text");
        when(service.getComment(anyInt())).thenReturn(comment);
        when(comment.getPostId()).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doGet(request,response);
        verify(service, times(1)).editComment(anyInt(), anyString());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("text")).thenReturn("text");
        when(service.getComment(anyInt())).thenReturn(comment);
        when(comment.getPostId()).thenReturn(1);
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        doReturn(servletContext).when(spyServlet).getServletContext();
        when(servletContext.getContextPath()).thenReturn("path");
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doGet(request,response));
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("text")).thenReturn("text");
        when(service.getComment(anyInt())).thenReturn(comment);
        when(comment.getPostId()).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doPost(request,response);
        verify(service, times(1)).editComment(anyInt(), anyString());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("text")).thenReturn("text");
        when(service.getComment(anyInt())).thenReturn(comment);
        when(comment.getPostId()).thenReturn(1);
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doPost(request,response));
    }


}