package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={DeleteBanServlet.class},
        cdiStereotypes = CdiMock.class)
public class DeleteBanServletTest extends ServletTest{

    @Mock private BanService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject DeleteBanServlet deleteBanServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("banId")).thenReturn("5");

        DeleteBanServlet spyServlet = spy(deleteBanServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doPost(request,response);
        verify(service, times(1)).removeBan(anyInt());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("banId")).thenReturn("-5");
        doThrow(ConstraintViolationException.class).when(service).removeBan(anyInt());

        assertThrows(ConstraintViolationException.class,() ->deleteBanServlet.doPost(request,response));
    }

    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("banId")).thenReturn("5");

        DeleteBanServlet spyServlet = spy(deleteBanServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doGet(request,response);
        verify(service, times(1)).removeBan(anyInt());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("banId")).thenReturn("-5");
        doThrow(ConstraintViolationException.class).when(service).removeBan(anyInt());

        assertThrows(ConstraintViolationException.class,() ->deleteBanServlet.doGet(request,response));
    }

}
