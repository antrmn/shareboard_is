package http.controller;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.ImageService;
import service.ServiceTest;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.MappingMatch;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={ImageServlet.class},
        cdiStereotypes = CdiMock.class)
class ImageServletTest extends ServiceTest {
    @Inject ImageServlet servlet;
    @Mock ImageService imageService;
    @Mock HttpServletMapping mapping;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    @BeforeEach
    void setMock(){
        when(request.getHttpServletMapping()).thenReturn(mapping);
    }

    @Test
    void emptyPathMatch() throws ServletException, IOException {
        when(mapping.getMappingMatch()).thenReturn(MappingMatch.EXACT);
        servlet.doGet(request,response);
        verify(response).setStatus(404);
    }

    @Test
    void imageNotFound() throws ServletException, IOException {
        when(mapping.getMappingMatch()).thenReturn(MappingMatch.PATH);
        when(mapping.getMatchValue()).thenReturn("filename");
        when(imageService.getImage("filename")).thenReturn(null);
        servlet.doGet(request,response);
        verify(response).setStatus(404);
    }

    @Test
    void imageFound() throws ServletException,IOException {
        byte[] bytes = "GIF8".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
        ServletOutputStream outputStream = mock(ServletOutputStream.class);

        when(mapping.getMappingMatch()).thenReturn(MappingMatch.PATH);
        when(mapping.getMatchValue()).thenReturn("filename");
        when(imageService.getImage("filename")).thenReturn(inputStream);
        when(response.getOutputStream()).thenReturn(outputStream);


        servlet.doGet(request, response);
        verify(response).setContentType("image/gif");
        verify(outputStream).write(any(),anyInt(),anyInt());
    }





}