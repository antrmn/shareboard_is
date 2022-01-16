package http.controller;

import service.FollowService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UnfollowServlet", value = "/UnfollowServlet")
public class UnfollowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sectionId = request.getParameter("section").matches("\\d*") ? Integer.parseInt(request.getParameter("section")) : 0;
        new FollowService().unFollow(sectionId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
