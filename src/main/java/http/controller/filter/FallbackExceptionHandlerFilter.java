package http.controller.filter;

import service.auth.AuthenticationRequiredException;
import service.auth.AuthorizationException;
import service.auth.BannedUserException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.*;

@WebFilter("*")
public class FallbackExceptionHandlerFilter extends HttpFilter {
    //low priority  todo: fare in modo che tutte le eccezioni non gestite vengano loggate da catalina

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try{
            chain.doFilter(req, resp);
        } catch (IllegalArgumentException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (ConstraintViolationException e) {
            String messages = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
            resp.sendError(SC_BAD_REQUEST, messages);
        } catch (AuthenticationRequiredException e) {
            resp.sendError(SC_UNAUTHORIZED, e.getMessage());
        } catch (BannedUserException e){
            if (e.getDuration() != null){
                String end  = DateTimeFormatter.ISO_INSTANT.format(e.getDuration());
                resp.sendError(SC_FORBIDDEN, "Sei bannato fino a " + end);
            } else {
                resp.sendError(SC_FORBIDDEN, e.getMessage());
            }
        } catch (AuthorizationException e) {
            resp.sendError(SC_FORBIDDEN, e.getMessage());
        }
    }
}
