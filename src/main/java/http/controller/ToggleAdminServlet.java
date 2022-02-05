package http.controller;

import http.util.ParameterConverter;
import service.UserService;
import service.dto.UserProfile;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/toggleAdmin")
public class ToggleAdminServlet extends HttpServlet {

    @Inject private ParameterConverter converter;
    @Inject private UserService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = converter.getIntParameter("userId").orElse(0);
        service.toggleAdmin(userId);
    }
}