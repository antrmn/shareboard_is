package http.controller;

import http.controller.interceptor.JSONError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import service.VoteService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vote")
public class VoteServlet extends InterceptableServlet {
    @Inject private VoteService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int id = converter.getIntParameter("id").orElse(0);
        String vote = request.getParameter("vote");
        String type = request.getParameter("type");

        if(type!=null && type.equalsIgnoreCase("post") && !vote.isEmpty()){
            if(vote.equalsIgnoreCase("upvote"))
                service.upvotePost(id);
            else if(vote.equalsIgnoreCase("downvote"))
                service.downvotePost(id);
            else
                throw new IllegalArgumentException();
        }else if(type!=null && type.equalsIgnoreCase("comment") && !vote.isEmpty()){
            if(vote.equalsIgnoreCase("upvote"))
                service.upvoteComment(id);
            else if(vote.equalsIgnoreCase("downvote"))
                service.downvoteComment(id);
            else
                throw new IllegalArgumentException();
        }else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    @JSONError
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
