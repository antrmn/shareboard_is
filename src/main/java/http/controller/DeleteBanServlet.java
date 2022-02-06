package http.controller;

import http.util.ParameterConverter;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/deleteban")
public class DeleteBanServlet extends HttpServlet {
    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int banId = converter.getIntParameter("banId").orElse(0);
        service.removeBan(banId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showbans?userId=" + request.getParameter("userId"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
