package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.AuthenticationService;
import service.PostService;
import service.UserService;
import service.dto.CurrentUser;

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
        value={RegisterServlet.class},
        cdiStereotypes = CdiMock.class)
public class RegisterServletTest extends ServletTest{

    @Mock private CurrentUser currentUser; //Mock necessario anche se inutilizzato
    @Mock private AuthenticationService authenticationService;
    @Mock private UserService service;
    @Mock private PostService postservice;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject RegisterServlet registerServlet;

    public RegisterServletTest() {
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("mail")).thenReturn("mail");
        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("pass")).thenReturn("pass");
        when(request.getParameter("pass2")).thenReturn("pass");
        when(request.getParameter("postedBefore")).thenReturn("date");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        RegisterServlet spyServlet = spy(registerServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        registerServlet.doPost(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).newUser(any(), any(), any());
        assertThrows(ConstraintViolationException.class,() -> registerServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        RegisterServlet spyServlet = spy(registerServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("1");
        RegisterServlet spyServlet = spy(registerServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        assertThrows(ConstraintViolationException.class,() -> spyServlet.doGet(request,response));
    }


}