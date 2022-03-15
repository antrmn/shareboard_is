package usecase.user;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={AddBanServlet.class},
        cdiStereotypes = CdiMock.class)
public class AddBanServletTest extends ServletTest {

    @Mock private BanService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject AddBanServlet addBanServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("endDate")).thenReturn("2022-02-06");
        when(request.getParameter("userId")).thenReturn("5");

        addBanServlet.doPost(request,response);
        verify(service, times(1)).addBan(any(),anyInt());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("endDate")).thenReturn("2021-02-06");
        when(request.getParameter("userId")).thenReturn("5");
        when(service.addBan(any(),anyInt())).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class,() -> addBanServlet.doPost(request,response));
    }

    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("endDate")).thenReturn("2022-02-06");
        when(request.getParameter("userId")).thenReturn("5");

        addBanServlet.doGet(request,response);
        verify(service, times(1)).addBan(any(),anyInt());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("endDate")).thenReturn("2021-02-06");
        when(request.getParameter("userId")).thenReturn("5");
        when(service.addBan(any(),anyInt())).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class,() -> addBanServlet.doGet(request,response));
    }

}
