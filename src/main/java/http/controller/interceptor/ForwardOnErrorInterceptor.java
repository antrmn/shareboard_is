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

public class ForwardOnErrorInterceptor extends ServletInterceptor<ForwardOnError> {

    private String page;

    @Override
    protected void init(ForwardOnError annotation) {
        this.page = annotation.value();
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
        try{
            next.handle(req,resp);
        } catch (IllegalArgumentException e) {
            forwardErrorPage(req, resp, SC_BAD_REQUEST, e.getMessage());
        } catch (ConstraintViolationException e) {
            List<String> collect = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            forwardErrorPage(req,resp,SC_BAD_REQUEST,collect);
        }
    }

    private void forwardErrorPage(HttpServletRequest req, HttpServletResponse resp,
                                  int code, Collection<String> messages) throws ServletException, IOException {
        req.setAttribute("errors", messages);
        resp.setStatus(code);
        req.getRequestDispatcher(page).forward(req, resp);
    }

    private void forwardErrorPage(HttpServletRequest req, HttpServletResponse resp, int code, String message) throws IOException, ServletException {
        List<String> singleton = message == null ? Collections.emptyList() : List.of(message);
        forwardErrorPage(req, resp, code, singleton);
    }

    private void forwardErrorPage(HttpServletRequest req, HttpServletResponse resp, int code) throws IOException, ServletException {
        forwardErrorPage(req, resp,code,Collections.emptyList());
    }
}