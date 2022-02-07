package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.User;
import rocks.limburg.cdimock.CdiMock;
import service.UserService;
import service.dto.UserProfile;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={UserServlet.class},
        cdiStereotypes = CdiMock.class)
public class UserServletTest extends ServletTest{

    @Mock private UserService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject UserServlet userServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("name")).thenReturn("test");
        userServlet.doGet(request,response);
        verify(service, times(1)).getUser(any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("name")).thenReturn("test");
        when(service.getUser(any())).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class,() -> userServlet.doGet(request,response));
    }


}