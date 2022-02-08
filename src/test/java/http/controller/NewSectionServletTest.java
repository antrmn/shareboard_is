package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.SectionService;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

;


@Classes(cdi = true,
        value={NewSectionServlet.class},
        cdiStereotypes = CdiMock.class)
@Disabled
public class NewSectionServletTest extends ServletTest{

    @Mock private SectionService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject NewSectionServlet sectionServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{

        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("description")).thenReturn("description");
//        when(request.getParameter("picture")).thenReturn("picture");
//        when(request.getParameter("banner")).thenReturn("banner");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        NewSectionServlet spyServlet = spy(sectionServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        sectionServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> sectionServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("description")).thenReturn("description");
//        when(request.getParameter("picture")).thenReturn("picture");
//        when(request.getParameter("banner")).thenReturn("banner");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        NewSectionServlet spyServlet = spy(sectionServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        sectionServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> sectionServlet.doGet(request,response));
    }


}