package http.controller;

import http.util.ParameterConverter;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/deleteban")
public class DeleteBanServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int banId = converter.getIntParameter("banId").orElse(0);
        service.removeBan(banId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showbans?userId=" + request.getParameter("userId"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
