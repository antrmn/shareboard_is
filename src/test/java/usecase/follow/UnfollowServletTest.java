package usecase.follow;


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
        value={UnfollowServlet.class},
        cdiStereotypes = CdiMock.class)
public class UnfollowServletTest extends ServletTest {

    @Mock private FollowService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject UnfollowServlet unfollowServlet;

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("usecase/section")).thenReturn("1");
        unfollowServlet.doPost(request,response);
        verify(service, times(1)).unFollow(anyInt());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("usecase/section")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).unFollow(anyInt());
        assertThrows(ConstraintViolationException.class,() -> unfollowServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("usecase/section")).thenReturn("1");
        unfollowServlet.doGet(request,response);
        verify(service, times(1)).unFollow(anyInt());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("usecase/section")).thenReturn("1");
        doThrow(ConstraintViolationException.class).when(service).unFollow(anyInt());
        assertThrows(ConstraintViolationException.class,() -> unfollowServlet.doGet(request,response));
    }


}