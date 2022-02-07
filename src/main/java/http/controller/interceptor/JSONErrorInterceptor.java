package http.controller.interceptor;

import http.util.interceptor.HttpServletBiConsumer;
import http.util.interceptor.ServletInterceptor;
import service.auth.AuthenticationRequiredException;
import service.auth.AuthorizationException;
import service.auth.BannedUserException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.*;

public class JSONErrorInterceptor extends ServletInterceptor<JSONError> {

    @Override
    protected void init(JSONError annotation) {

    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
        try{
            next.handle(req,resp);
        } catch (IllegalArgumentException e) {
            sendJSONError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (ConstraintViolationException e) {
            List<String> collect = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            sendJSONError(resp,SC_BAD_REQUEST,collect);
        } catch (AuthenticationRequiredException e) {
            sendJSONError(resp,SC_UNAUTHORIZED, e.getMessage());
        } catch (BannedUserException e){
            if (e.getDuration() != null){
                String end  = DateTimeFormatter.ISO_INSTANT.format(e.getDuration());
                sendJSONError(resp,SC_FORBIDDEN, "Sei bannato fino a " + end);
            } else {
                sendJSONError(resp, SC_FORBIDDEN);
            }
        } catch (AuthorizationException e) {
            sendJSONError(resp,SC_FORBIDDEN, e.getMessage());
        } catch (RuntimeException | ServletException | IOException e){
            sendJSONError(resp,SC_INTERNAL_SERVER_ERROR, "Internal server error");
            req.getServletContext().log(e.getMessage(), e);
        }
    }

    private void sendJSONError(HttpServletResponse resp, int code, Collection<String> messages) throws IOException {
        JsonObject errors = Json.createObjectBuilder().add("errors", Json.createArrayBuilder(messages)).build();
        resp.setStatus(code);
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.print(errors.toString());
        writer.flush();
    }

    private void sendJSONError(HttpServletResponse resp, int code, String message) throws IOException {
        List<String> singleton = message == null ? Collections.emptyList() : List.of(message);
        sendJSONError(resp, code, singleton);
    }

    private void sendJSONError(HttpServletResponse resp, int code) throws IOException{
        sendJSONError(resp,code,Collections.emptyList());
    }

    @Override
    public int priority() {
        return 2;
    }
}
