package http.controller;

import service.UserService;
import service.dto.LoggedInUser;
import service.exception.BadRequestException;
import service.exception.ServiceException;

import javax.inject.Inject;
import javax.mvc.binding.BindingResult;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Inject private UserService userService;
    @Inject private LoggedInUser loggedInUser;

    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(loggedInUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
        }
        view(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(loggedInUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
        }
        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String password = req.getParameter("pass");
        String confirmPassword = req.getParameter("pass2");

        if(!Objects.equals(password, confirmPassword)){
            //
        }

        try{
            userService.newUser(email, username, password);
        } catch (BadRequestException e) {
            req.setAttribute("errors", e.getMessages());
            view(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath());
    }
}
