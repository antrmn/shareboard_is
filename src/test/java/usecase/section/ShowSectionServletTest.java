package usecase.section;


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
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={ShowSectionServlet.class},
        cdiStereotypes = CdiMock.class)
public class ShowSectionServletTest extends ServletTest {

    @Mock private SectionService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject ShowSectionServlet showSectionsServlet;


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        showSectionsServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> showSectionsServlet.doGet(request,response));
    }


}