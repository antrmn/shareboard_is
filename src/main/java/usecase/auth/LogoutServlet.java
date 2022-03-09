package usecase.auth;

import common.http.interceptor.InterceptableServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet dedicata alle operazioni di logout.
 */
@WebServlet("/logout")
class LogoutServlet extends InterceptableServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession(true).invalidate();
        resp.sendRedirect(req.getContextPath());
    }
}
