package usecase.user;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

/**
 * Servlet accessibile soltanto agli amministratori per la rimozione dei ban.
 */
@WebServlet("/admin/deleteban")
@AuthorizationConstraints(ADMINS_ONLY)
class DeleteBanServlet extends InterceptableServlet {
    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int banId = converter.getIntParameter("banId").orElse(0);
        service.removeBan(banId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showbans?userId=" + request.getParameter("userId"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
