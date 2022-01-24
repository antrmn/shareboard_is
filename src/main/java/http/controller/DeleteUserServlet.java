package http.controller;

import http.util.ParameterConverter;
import service.UserService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/deleteuser")
public class DeleteUserServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private UserService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = converter.getIntParameter("userId").orElse(0);
        service.delete(userId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showusers");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
