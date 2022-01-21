package http.controller;

import persistence.model.User;
import service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");
        User user = new UserService().getUser(username);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/user-profile.jsp").forward(request, response);
    }
}
