package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.FollowService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={FollowServlet.class},
        cdiStereotypes = CdiMock.class)
public class FollowServletTest extends ServletTest{

    @Mock private FollowService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject FollowServlet followServlet;

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        followServlet.doPost(request,response);
        verify(service, times(1)).follow(anyInt());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).follow(anyInt());
        assertThrows(ConstraintViolationException.class,() -> followServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        followServlet.doGet(request,response);
        verify(service, times(1)).follow(anyInt());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).follow(anyInt());
        assertThrows(ConstraintViolationException.class,() -> followServlet.doGet(request,response));
    }


}