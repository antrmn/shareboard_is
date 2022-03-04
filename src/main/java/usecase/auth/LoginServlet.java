package usecase.auth;

import common.http.interceptor.InterceptableServlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
class LoginServlet extends InterceptableServlet {
    @Inject AuthenticationService authenticationService;
    @Inject CurrentUser currentUser;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(currentUser.isLoggedIn())
            resp.sendRedirect(req.getContextPath());
        else
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(currentUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }

        String username = Optional.ofNullable(req.getParameter("username")).orElse("");
        String password = Optional.ofNullable(req.getParameter("pass")).orElse("");

        boolean loginSuccessful = authenticationService.authenticate(username, password);

        if(loginSuccessful)
            resp.sendRedirect(req.getContextPath());
        else
            req.getRequestDispatcher("/WEB-INF/views/login.jsp?error").forward(req, resp);
    }
}
