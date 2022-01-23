package http.controller;

import service.PostService;
import service.dto.PostPage;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeletePostServlet", value = "/DeletePostServlet")
public class DeletePostServlet extends HttpServlet {

    @Inject private PostService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _postId = request.getParameter("id");

        int postId = 0;
        if(_postId != null && _postId.matches("\\d*")){
            postId = Integer.parseInt(_postId);
        }
        String sectionName = service.getPost(postId).getSection().getName();
        service.delete(postId);
        response.sendRedirect(getServletContext().getContextPath() + "/s/" + sectionName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
