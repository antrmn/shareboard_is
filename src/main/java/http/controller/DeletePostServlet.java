package http.controller;

import http.util.ParameterConverter;
import service.PostService;
import service.dto.PostPage;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeletePostServlet", value = "/DeletePostServlet")
public class DeletePostServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private PostService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = converter.getIntParameter("id").orElse(0);
        String sectionName = service.getPost(postId).getSection().getName();
        service.delete(postId);
        response.sendRedirect(getServletContext().getContextPath() + "/s/" + sectionName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
