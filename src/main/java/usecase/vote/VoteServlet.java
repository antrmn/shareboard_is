package usecase.vote;

import common.http.ParameterConverter;
import common.http.error.JSONError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet("/vote")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class VoteServlet extends InterceptableServlet {
    @Inject private VoteService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int id = converter.getIntParameter("id").orElse(0);
        String vote = request.getParameter("vote");
        String type = request.getParameter("type");

        if("post".equalsIgnoreCase(type)){
            if("upvote".equalsIgnoreCase(vote))
                service.upvotePost(id);
            else if("downvote".equalsIgnoreCase(vote))
                service.downvotePost(id);
            else
                throw new IllegalArgumentException();
        }else if("comment".equalsIgnoreCase(type)){
            if("upvote".equalsIgnoreCase(vote))
                service.upvoteComment(id);
            else if("downvote".equalsIgnoreCase(vote))
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
