package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.CommentService;

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
    @Inject EditCommentServlet editCommentServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{

        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("text")).thenReturn("text");

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        editCommentServlet.doGet(request,response);
        verify(service, times(1)).editComment(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        assertThrows(ConstraintViolationException.class,() -> editCommentServlet.doGet(request,response));
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("pass")).thenReturn("pass");
        EditCommentServlet spyServlet = spy(editCommentServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        editCommentServlet.doPost(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        assertThrows(ConstraintViolationException.class,() -> editCommentServlet.doPost(request,response));
    }


}