package usecase.post;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet(name = "DeletePostServlet", value = "/DeletePostServlet")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
public class DeletePostServlet extends InterceptableServlet {
    @Inject private PostService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int postId = converter.getIntParameter("id").orElse(0);
        String sectionName = service.getPost(postId).getSectionName();
        service.delete(postId);
        response.sendRedirect(getServletContext().getContextPath() + "/s/" + sectionName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
