package http.controller.interceptor;

import http.util.interceptor.InterceptableServlet;
import http.util.interceptor.ServletInterceptorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import service.auth.AuthenticationRequiredException;
import service.auth.AuthorizationException;
import service.auth.BannedUserException;
import service.dto.CurrentUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

import static http.controller.interceptor.AuthorizationConstraints.Types.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorizationConstraintsInterceptorTest{

    @BeforeAll
    void register(){
        try{
            ServletInterceptorFactory.register(AuthorizationConstraintsInterceptor.class);
        }catch(IllegalArgumentException e){
            // già registrato? va bene lo stesso
        }
    }

    @Test
    void guestAttemptsAdminsOnlyResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(false).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("GET");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertThrows(AuthenticationRequiredException.class, () -> servlet.service(request,response));
    }

    @Test
    void userAttemptsAdminsOnlyResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(true).isAdmin(false).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("GET");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertThrows(AuthorizationException.class, () -> servlet.service(request,response));
    }

    @Test
    void adminAttemptsAdminOnlyResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(true).isAdmin(true).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("GET");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertDoesNotThrow(() -> servlet.service(request,response));
    }


    @Test
    void guestAttemptsAuthenticationRequiredResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(false).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("POST");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertThrows(AuthenticationRequiredException.class, () -> servlet.service(request,response));
    }

    @Test
    void authenticatedUserAttemptsAuthenticationRequiredResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(true).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("POST");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertDoesNotThrow(() -> servlet.service(request,response));
    }



    @Test
    void bannedUserAttemptsResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(true).banDuration(Instant.MAX).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("PUT");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertThrows(BannedUserException.class, () -> servlet.service(request,response));
    }

    @Test
    void notBannedUserAttemptsResource(){
        CurrentUser currentUser = CurrentUser.builder().isLoggedIn(true).banDuration(null).build();
        SampleInterceptableServlet servlet = new SampleInterceptableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        when(request.getMethod()).thenReturn("PUT");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertDoesNotThrow(() -> servlet.service(request,response));
    }





    private static class SampleInterceptableServlet extends InterceptableServlet{

        @AuthorizationConstraints({ADMINS_ONLY})
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }

        @Override
        @AuthorizationConstraints({REQUIRE_AUTHENTICATION})
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }

        @Override
        @AuthorizationConstraints({DENY_BANNED_USERS})
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }
    }

}