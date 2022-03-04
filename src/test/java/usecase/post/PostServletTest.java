package usecase.post;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServletTest;
import usecase.comment.CommentDTO;
import usecase.comment.CommentService;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

;


@Classes(cdi = true,
        value={PostServlet.class},
        cdiStereotypes = CdiMock.class)
public class PostServletTest extends ServletTest {

    @Mock private CommentService commentService;
    @Mock private PostService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;
    @Inject PostServlet postServlet;


    @Test
    void successfulldoPost() throws ServletException, IOException{

        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("usecase/comment")).thenReturn("0");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        PostServlet spyServlet = Mockito.spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void successfulldoPostWithCommentId() throws ServletException, IOException{

        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("usecase/comment")).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        Map<Integer, List<CommentDTO>> comments = new HashMap<>();
        List<CommentDTO> list = new ArrayList<>();
        CommentDTO commentDTO = new CommentDTO(1,"author",2,"content", Instant.now(),3,4,1,3);
        list.add(commentDTO);
        comments.put(1,list);
        doReturn(comments).when(commentService).getReplies(anyInt());

        PostServlet spyServlet = Mockito.spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doPost(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoPost() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(service).getPost(anyInt());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doPost(request,response));
    }


    @Test
    void successfulldoGet() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("usecase/comment")).thenReturn("0");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);


        PostServlet spyServlet = Mockito.spy(postServlet);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("path");
        doReturn(servletContext).when(spyServlet).getServletContext();
        postServlet.doGet(request,response);
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    void faildoGet() throws ServletException, IOException{
        doThrow(ConstraintViolationException.class).when(service).getPost(anyInt());
        assertThrows(ConstraintViolationException.class,() -> postServlet.doGet(request,response));
    }


}