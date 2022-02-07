package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.SectionService;

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
        value={SectionServlet.class},
        cdiStereotypes = CdiMock.class)
public class SectionServletTest extends ServletTest{

    @Mock private SectionService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject SectionServlet sectionServlet;

    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("sectionName");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        sectionServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("sectionName");
        doThrow(ConstraintViolationException.class).when(service).getSection(anyString());
        assertThrows(ConstraintViolationException.class,() -> sectionServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("sectionName");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        sectionServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("sectionName");
        doThrow(ConstraintViolationException.class).when(service).getSection(anyString());
        assertThrows(ConstraintViolationException.class,() -> sectionServlet.doGet(request,response));
    }


}