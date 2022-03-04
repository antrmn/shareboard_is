package common.http;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={AliasFilter.class},
        cdiStereotypes = CdiMock.class)
class AliasFilterTest extends ServiceTest {
    @Mock CurrentUser currentUser;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock RequestDispatcher dispatcher;
    @Inject AliasFilter aliasFilter;

    @BeforeEach
    void setMock(){
        when(request.getContextPath()).thenReturn("webapp");
        when(request.getQueryString()).thenReturn("test=test");
        when(request.getRequestDispatcher(any())).thenReturn(dispatcher);
    }

    @Test
    void invalidURI() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(";|;|;|;|;||;|;;|");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request,never()).getRequestDispatcher(any());
        verify(dispatcher,never()).forward(request,response);
    }

    @Test
    void authenticatedMe() throws ServletException, IOException {
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(currentUser.getUsername()).thenReturn("username");

        when(request.getRequestURI()).thenReturn("webapp/me");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/user?name=username&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void unauthenticatedMe() throws ServletException, IOException {
        when(currentUser.isLoggedIn()).thenReturn(false);

        when(request.getRequestURI()).thenReturn("webapp/me");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(response).sendError(401);
    }

    @Test
    void viewPopular() throws ServletException,IOException{
        when(request.getRequestURI()).thenReturn("webapp/popular");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/home?view=popular&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void viewFeed() throws ServletException,IOException{
        when(request.getRequestURI()).thenReturn("webapp/feed");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/home?view=following&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void viewSection() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/s/mysection");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/s?section=mysection&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void viewSectionEmptyArg() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/s/");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request,never()).getRequestDispatcher(any());
        verify(dispatcher,never()).forward(request,response);
    }


    @Test
    void viewUser() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/u/utente");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/user?name=utente&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void viewUserEmptyArg() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/u/");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request, never()).getRequestDispatcher(any());
        verify(dispatcher,never()).forward(request,response);
    }

    @Test
    void viewPost() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/post/1");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request).getRequestDispatcher("/post?id=1&test=test");
        verify(dispatcher).forward(request,response);
    }

    @Test
    void viewPostEmptyArg() throws ServletException, IOException{
        when(request.getRequestURI()).thenReturn("webapp/post/");
        aliasFilter.doFilter(request, response, (req,resp)->{});
        verify(request,never()).getRequestDispatcher(any());
        verify(dispatcher,never()).forward(request,response);
    }
}