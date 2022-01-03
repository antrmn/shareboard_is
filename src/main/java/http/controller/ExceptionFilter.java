package http.controller;

import service.exception.AuthenticationRequiredException;
import service.exception.BadRequestException;
import service.exception.ForbiddenException;
import service.exception.ServiceException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static javax.servlet.http.HttpServletResponse.*;

public class ExceptionFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try{
            chain.doFilter(req, resp);
        } catch (BadRequestException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (ForbiddenException e) {
            resp.sendError(SC_FORBIDDEN, e.getMessage());
        } catch (AuthenticationRequiredException e) {
            resp.sendError(SC_UNAUTHORIZED, e.getMessage());
        } catch (ServiceException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        }

    }
}
