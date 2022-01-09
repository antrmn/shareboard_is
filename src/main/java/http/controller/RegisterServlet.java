package http.controller;

import org.apache.openejb.util.reflection.Reflections;
import service.UserService;
import service.dto.LoggedInUser;
import service.exception.BadRequestException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@WebServlet("/register")
public class RegisterServlet extends FormPageServlet {
    @Inject private UserService userService;
    @Inject private LoggedInUser loggedInUser;

    @Override
    protected void viewPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(loggedInUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void viewErrorpage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        viewPage(req, resp);
    }

    @Override
    protected void submitForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        if(loggedInUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String password = req.getParameter("pass");
        String confirmPassword = req.getParameter("pass2");

        if(!Objects.equals(password, confirmPassword)){
            throw new BadRequestException("Le password devono coincidere");
        }
        userService.newUser(email, username, password);
        resp.sendRedirect(req.getContextPath());
    }
}
