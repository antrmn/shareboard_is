package http.controller;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.PostService;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={EditPostServlet.class},
        cdiStereotypes = CdiMock.class)
public class EditPostServletTest extends ServletTest{

    @Mock private PostService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Mock private Part part;
    @Inject EditPostServlet editPostServlet;

    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("sectionId")).thenReturn("1");
        EditPostServlet spyServlet = spy(editPostServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        EditPostServlet spyServlet = spy(editPostServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();

        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doGet(request,response));
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("title");
        when(request.getParameter("content")).thenReturn("content");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditPostServlet spyServlet = spy(editPostServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doPost(request,response);
        verify(service, times(1)).editPost(any(), anyInt(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("title");
        when(request.getParameter("content")).thenReturn("content");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditPostServlet spyServlet = spy(editPostServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        doThrow(ConstraintViolationException.class).when(service).editPost(any(), anyInt(), any());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doPost(request,response));
    }


}