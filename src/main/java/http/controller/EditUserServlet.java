package http.controller;

import http.controller.interceptor.ForwardOnError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import persistence.model.User;
import service.UserService;
import service.dto.UserEditPage;
import service.dto.UserProfile;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet("/edituser")
@MultipartConfig
public class EditUserServlet extends InterceptableServlet {
    @Inject private ParameterConverter converter;
    @Inject private UserService service;

    private static final String EDIT_USER_PAGE = "/WEB-INF/views/edit-user.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = converter.getIntParameter("id").orElse(0);
        UserProfile user = service.getUser(userId);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(request, response);
    }

    @Override
    @ForwardOnError(EDIT_USER_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = converter.getIntParameter("id").orElse(0);
        String email = request.getParameter("email");
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        String pass = request.getParameter("pass");
        String pass2 = request.getParameter("pass2");

        if((!pass.isEmpty() || !pass2.isEmpty()) && !pass.equals(pass2))
            throw new IllegalArgumentException("Le password devono coincidere");

        BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
        UserEditPage userEditPage = new UserEditPage(userId,description,email,buffPicture,pass);
        service.edit(userEditPage,userId);

        response.sendRedirect(request.getContextPath() + "/u/" + service.getUsernameById(userId));
    }
}
