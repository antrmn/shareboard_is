package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.PostService;
import service.SectionService;
import service.dto.SectionPage;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={NewPostServlet.class},
        cdiStereotypes = CdiMock.class)

public class NewPostServletTest extends ServletTest{

    @Mock private PostService service;
    @Mock private SectionService sectionService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Mock private SectionPage section;
    @Mock private Part part;
    @Inject NewPostServlet postServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("text");
        when(request.getParameter("content")).thenReturn("text");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn(Long.valueOf(0));
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doPost(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void successfulldoPostWithImage() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("image");
        when(request.getParameter("content")).thenReturn("text");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn(Long.valueOf(0));
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        spyServlet.doPost(request,response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void faildoPostWithImageTooLarge() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("image");
        when(request.getParameter("content")).thenReturn("text");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn((long) (6*1024*1024));
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        assertThrows(IllegalArgumentException.class,() -> spyServlet.doPost(request,response));
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("text");
        when(request.getParameter("content")).thenReturn("text");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn(Long.valueOf(0));
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();

        doThrow(ConstraintViolationException.class).when(service).newPost(anyString(),anyString(), anyString());
        doThrow(ConstraintViolationException.class).when(service).newPost(anyString(), any(),anyLong(), anyString());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();
        doReturn(servletContext).when(spyServlet).getServletContext();

        doThrow(ConstraintViolationException.class).when(dispatcher).forward(any(), any());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doGet(request,response));
    }

    @Test
    void tooBigImage() throws ServletException,IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("img");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn(Long.valueOf(6*1024*1024));
        when(part.getInputStream()).thenReturn(new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))));

        assertThrows(IllegalArgumentException.class, () -> postServlet.doPost(request,response));
    }

    @Test
    void successfulImage() throws ServletException,IOException{
        when(request.getParameter("section")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("text");
        when(request.getParameter("type")).thenReturn("img");
        when(sectionService.showSection(anyInt())).thenReturn(section);
        when(section.getName()).thenReturn("section");
        when(request.getPart(anyString())).thenReturn(part);
        when(part.getName()).thenReturn("part");
        when(part.getSize()).thenReturn(Long.valueOf(1));
        BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
        when(part.getInputStream()).thenReturn(inputStream);

        NewPostServlet spyServlet = spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doReturn(servletContext).when(spyServlet).getServletContext();

        spyServlet.doPost(request,response);
        verify(service).newPost(anyString(),any(),anyLong(), anyString());
    }

}
