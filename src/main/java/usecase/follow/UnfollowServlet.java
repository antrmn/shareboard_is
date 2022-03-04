package usecase.follow;

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

@WebServlet("/unfollow")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
public class UnfollowServlet extends InterceptableServlet {
    @Inject private FollowService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int sectionId = converter.getIntParameter("section").orElse(0);
        service.unFollow(sectionId);
    }

    @Override
    @JSONError
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
