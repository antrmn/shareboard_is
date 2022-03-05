package usecase.user;

import common.http.ParameterConverter;
import common.http.error.JSONError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/toggleAdmin")
@AuthorizationConstraints(ADMINS_ONLY)
public class ToggleAdminServlet extends InterceptableServlet {

    @Inject private UserService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(req);
        int userId = converter.getIntParameter("userId").orElse(0);
        service.toggleAdmin(userId);
    }
}