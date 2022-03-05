package usecase.user;

import common.http.interceptor.InterceptableServlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
class UserServlet extends InterceptableServlet {

    @Inject
    private UserService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");
        UserProfile user = service.getUser(username);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/user-profile.jsp").forward(request, response);
    }
}
