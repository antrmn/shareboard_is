package http.controller;

import http.controller.interceptor.JSONError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/toggleAdmin")
public class ToggleAdminServlet extends InterceptableServlet {

    @Inject private UserService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(req);
        int userId = converter.getIntParameter("userId").orElse(0);
        service.toggleAdmin(userId);
    }
}