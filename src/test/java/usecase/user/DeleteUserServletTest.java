package usecase.user;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={DeleteUserServlet.class},
        cdiStereotypes = CdiMock.class)
public class DeleteUserServletTest extends ServletTest {

    @Mock private UserService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject private DeleteUserServlet servlet;

    @Test
    void successfulDoGet() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("2");

        DeleteUserServlet spyServlet = Mockito.spy(servlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doGet(request,response);
        verify(service, times(1)).delete(anyInt());
    }

    @Test
    void failDoGet() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("-2");
        doThrow(ConstraintViolationException.class).when(service).delete(anyInt());
        assertThrows(ConstraintViolationException.class,() ->servlet.doGet(request,response));
    }

    @Test
    void successfulDoPost() throws ServletException, IOException {
        when(request.getParameter("userId")).thenReturn("2");

        DeleteUserServlet spyServlet = Mockito.spy(servlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doPost(request,response);
        verify(service, times(1)).delete(anyInt());
    }

    @Test
    void failDoPost() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("-2");
        doThrow(ConstraintViolationException.class).when(service).delete(anyInt());
        assertThrows(ConstraintViolationException.class,() ->servlet.doGet(request,response));
    }


}