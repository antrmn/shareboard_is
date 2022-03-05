package usecase.user;

import common.http.error.ForwardOnError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthenticationService;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


@WebServlet("/register")
class RegisterServlet extends InterceptableServlet {
    @Inject private UserService userService;
    @Inject private CurrentUser currentUser;
    @Inject private AuthenticationService authenticationService;

    private static final String REGISTER_PAGE = "/WEB-INF/views/register.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(currentUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.getRequestDispatcher(REGISTER_PAGE).forward(req, resp);
    }


    @Override
    @ForwardOnError(REGISTER_PAGE)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(currentUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String password = req.getParameter("pass");
        String confirmPassword = req.getParameter("pass2");

        if(!Objects.equals(password, confirmPassword)){
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Le password devono coincidere");
            req.setAttribute("errors",errors);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }else {
            userService.newUser(email, username, password);
            authenticationService.authenticate(username,password);
            resp.sendRedirect(req.getContextPath());
        }
    }
}
