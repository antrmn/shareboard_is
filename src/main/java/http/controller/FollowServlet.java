package http.controller;

import http.util.ParameterConverter;
import service.FollowService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private FollowService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sectionId = converter.getIntParameter("section").orElse(0);
        service.follow(sectionId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
