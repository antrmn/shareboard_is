package http.controller;

import http.util.ParameterConverter;
import service.VoteService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/unvote")
public class UnvoteServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private VoteService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = converter.getIntParameter("id").orElse(0);
        String type = request.getParameter("type");

        if(type!=null && type.equalsIgnoreCase("post")){
            service.unvotePost(id);
        }else if(type!=null && type.equalsIgnoreCase("comment")){
            service.unvoteComment(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
