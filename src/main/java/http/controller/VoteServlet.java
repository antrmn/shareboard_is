package http.controller;

import service.VoteService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _id = request.getParameter("id");
        String vote = request.getParameter("vote");
        String type = request.getParameter("type");

        if(type!=null && type.equalsIgnoreCase("post")){
            new VoteService().VotePost(_id,vote);
        }else if(type!=null && type.equalsIgnoreCase("comment")){
            new VoteService().VoteComment(_id,vote);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
