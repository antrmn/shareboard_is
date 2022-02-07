package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.FollowService;
import service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={ToggleAdminServlet.class},
        cdiStereotypes = CdiMock.class)
public class ToggleAdminServletTest extends ServletTest{

    @Mock private UserService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject ToggleAdminServlet toggleAdminServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("userId")).thenReturn("1");
        toggleAdminServlet.doGet(request,response);
        verify(service, times(1)).toggleAdmin(anyInt());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).toggleAdmin(anyInt());
        assertThrows(ConstraintViolationException.class,() -> toggleAdminServlet.doGet(request,response));
    }


}