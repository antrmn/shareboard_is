package http.controller;


import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import service.VoteService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Classes(cdi = true,
        value={UnvoteServlet.class},
        cdiStereotypes = CdiMock.class)
public class UnvoteServletTest extends ServletTest{

    @Mock private VoteService service;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject UnvoteServlet unvoteServlet;


    @ParameterizedTest
    @ValueSource(strings = {"post", "comment"})
    void successfulldoPost(String type) throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("type")).thenReturn(type);

        unvoteServlet.doPost(request,response);

        if(type.equalsIgnoreCase("post") ){
            verify(service, times(1)).unvotePost(anyInt());
        }else if(type.equalsIgnoreCase("comment")){
            verify(service, times(1)).unvoteComment(anyInt());
        }
    }

    @ParameterizedTest
    @CsvSource({"post,upvote", "post,downvote", "comment,upvote", "comment,downvote"})
    void faildoPost(String type, String vote) throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("type")).thenReturn(type);
        when(request.getParameter("vote")).thenReturn(vote);

        doThrow(ConstraintViolationException.class).when(service).unvotePost(anyInt());
        doThrow(ConstraintViolationException.class).when(service).unvoteComment(anyInt());
        assertThrows(ConstraintViolationException.class,() -> unvoteServlet.doPost(request,response));
    }

    @ParameterizedTest
    @ValueSource(strings = {"post", "comment"})
    void successfulldoGet(String type) throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("type")).thenReturn(type);

        unvoteServlet.doGet(request,response);

        if(type.equalsIgnoreCase("post") ){
            verify(service, times(1)).unvotePost(anyInt());
        }else if(type.equalsIgnoreCase("comment")){
            verify(service, times(1)).unvoteComment(anyInt());
        }
    }

    @ParameterizedTest
    @CsvSource({"post,upvote", "post,downvote", "comment,upvote", "comment,downvote"})
    void faildoGet(String type, String vote) throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("type")).thenReturn(type);
        when(request.getParameter("vote")).thenReturn(vote);

        doThrow(ConstraintViolationException.class).when(service).unvotePost(anyInt());
        doThrow(ConstraintViolationException.class).when(service).unvoteComment(anyInt());
        assertThrows(ConstraintViolationException.class,() -> unvoteServlet.doGet(request,response));
    }

    @Test
    void typeNotDefined() throws ServletException, IOException{
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("nope");

        assertThrows(IllegalArgumentException.class,() -> unvoteServlet.doPost(request,response));
    }

}