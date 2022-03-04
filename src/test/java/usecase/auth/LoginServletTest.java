package usecase.auth;


import org.apache.openejb.testing.Classes;
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
        value={LoginServlet.class},
        cdiStereotypes = CdiMock.class)
public class LoginServletTest extends ServletTest {

    @Mock AuthenticationService authenticationService;
    @Mock CurrentUser currentUser;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject LoginServlet loginServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        LoginServlet spyServlet = Mockito.spy(loginServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        loginServlet.doGet(request,response);
        if(currentUser.isLoggedIn())
            verify(response, times(1)).sendRedirect(any());
        else
            verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> loginServlet.doGet(request,response));
    }

    //TODO: CHECK
    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("pass")).thenReturn("pass");
        LoginServlet spyServlet = Mockito.spy(loginServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(authenticationService.authenticate(any(), any())).thenReturn(false);
        doReturn(servletContext).when(spyServlet).getServletContext();
        loginServlet.doPost(request,response);
        if(currentUser.isLoggedIn())
            verify(response, times(1)).sendRedirect(any());
        else
            verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(response).sendRedirect(any());
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> loginServlet.doPost(request,response));
    }

    @Test
    void alreadyLoggedIdDoget() throws ServletException, IOException {
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(request.getContextPath()).thenReturn("cont");
        loginServlet.doGet(request,response);
        verify(response).sendRedirect("cont");
    }

    @Test
    void alreadyLoggedIdDoPost() throws ServletException, IOException {
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(request.getContextPath()).thenReturn("cont");
        loginServlet.doPost(request,response);
        verify(response).sendRedirect("cont");
    }

    @Test
    void successfulLogin() throws ServletException, IOException {
        when(currentUser.isLoggedIn()).thenReturn(false);
        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("pass")).thenReturn("pass");
        when(authenticationService.authenticate("username", "pass")).thenReturn(true);

        when(request.getContextPath()).thenReturn("cont");
        loginServlet.doPost(request,response);
        verify(response).sendRedirect("cont");
    }

}