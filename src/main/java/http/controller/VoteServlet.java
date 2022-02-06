package http.controller;

import http.util.ParameterConverter;
import service.VoteService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    @Inject private VoteService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int id = converter.getIntParameter("id").orElse(0);
        int vote = converter.getIntParameter("vote").orElse(0);
        String type = request.getParameter("type");

        if(type!=null && type.equalsIgnoreCase("post")){
            if(vote == +1)
                service.upvotePost(id);
            else if(vote == -1)
                service.downvotePost(id);
            else
                throw new IllegalArgumentException();
        }else if(type!=null && type.equalsIgnoreCase("comment")){
            if(vote == +1)
                service.upvoteComment(id);
            else if(vote == -1)
                service.downvoteComment(id);
            else
                throw new IllegalArgumentException();
        }else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
