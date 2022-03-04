package usecase.post;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={SearchPostServlet.class},
        cdiStereotypes = CdiMock.class)
public class SearchPostServletTest extends ServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject
    SearchPostServlet searchPostServlet;

    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        searchPostServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        searchPostServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }
}