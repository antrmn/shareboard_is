package usecase.user;

import common.http.ParameterConverter;
import common.http.error.ForwardOnError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;
import usecase.auth.AuthorizationException;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet("/edituser")
@MultipartConfig
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
public class EditUserServlet extends InterceptableServlet {
    @Inject private UserService service;
    @Inject private CurrentUser currentUser;

    private static final String EDIT_USER_PAGE = "/WEB-INF/views/edit-user.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int userId = converter.getIntParameter("id").orElse(0);
        if(!currentUser.isAdmin() && currentUser.getId() != userId)
            throw new AuthorizationException();
        UserProfile user = service.getUser(userId);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(request, response);
    }

    @Override
    @ForwardOnError(EDIT_USER_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int userId = converter.getIntParameter("id").orElse(0);
        String email = request.getParameter("email");
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        String pass = request.getParameter("pass");
        String pass2 = request.getParameter("pass2");

        if((!pass.isEmpty() || !pass2.isEmpty()) && !pass.equals(pass2))
            throw new IllegalArgumentException("Le password devono coincidere");

        BufferedInputStream stream = picture.getSize() > 0 ? new BufferedInputStream(picture.getInputStream()) : null;
        UserEditPage userEditPage = new UserEditPage(description,email,stream ,pass);
        service.edit(userEditPage,userId);

        response.sendRedirect(request.getContextPath() + "/u/" + service.getUsernameById(userId));
    }
}
