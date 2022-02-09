package http.controller;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.UserService;

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
        value={EditUserServlet.class},
        cdiStereotypes = CdiMock.class)
public class EditUserServletTest extends ServletTest{

    @Mock private UserService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Mock private Part part;
    @Inject EditUserServlet editUserServlet;

    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        EditUserServlet spyServlet = spy(editUserServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        EditUserServlet spyServlet = spy(editUserServlet);
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
        when(request.getParameter("email")).thenReturn("email");
        when(request.getParameter("description")).thenReturn("description");
        when(request.getParameter("pass")).thenReturn("pass");
        when(request.getParameter("pass2")).thenReturn("pass");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditUserServlet spyServlet = spy(editUserServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doPost(request,response);
        verify(service, times(1)).edit(any(), anyInt());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("email")).thenReturn("email");
        when(request.getParameter("description")).thenReturn("description");
        when(request.getParameter("pass")).thenReturn("pass");
        when(request.getParameter("pass2")).thenReturn("pass");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        EditUserServlet spyServlet = spy(editUserServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        doThrow(ConstraintViolationException.class).when(service).edit(any(), anyInt());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doPost(request,response));
    }


}