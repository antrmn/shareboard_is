package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={AdminServlet.class},
        cdiStereotypes = CdiMock.class)
public class AdminServletTest extends ServletTest{

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject AdminServlet adminServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        adminServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> adminServlet.doGet(request,response));
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        adminServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> adminServlet.doPost(request,response));
    }


}
