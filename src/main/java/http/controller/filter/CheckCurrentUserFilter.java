package http.controller.filter;

import service.dto.CurrentUser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*")
public class CheckCurrentUserFilter extends HttpFilter {
    @Inject @Named("currentUser") CurrentUser currentUser;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute("currentUser",currentUser);
        super.doFilter(request, response, chain);
    }
}
