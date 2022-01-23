package http.controller;

import service.FollowService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/unfollow")
public class UnfollowServlet extends HttpServlet {

    @Inject private FollowService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _sectionId = request.getParameter("section");
        int sectionId = 0;
        if(_sectionId != null && _sectionId.matches("\\d*")){
            sectionId = Integer.parseInt(_sectionId);
        }
        service.unFollow(sectionId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
