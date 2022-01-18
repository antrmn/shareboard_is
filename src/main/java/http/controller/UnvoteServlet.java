package http.controller;

import service.VoteService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/unvote")
public class UnvoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _id = request.getParameter("id");
        String type = request.getParameter("type");

        if(type!=null && type.equalsIgnoreCase("post")){
            new VoteService().UnvotePost(_id);
        }else if(type!=null && type.equalsIgnoreCase("comment")){
            new VoteService().UnvoteComment(_id);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
