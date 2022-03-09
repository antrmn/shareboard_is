package usecase.user;

import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

/**
 * Servlet che mostra agli amministratori gli utenti registrati.
 */
@WebServlet("/admin/showusers")
@AuthorizationConstraints(ADMINS_ONLY)
class ShowUsersServlet extends InterceptableServlet {

    @Inject private UserService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserProfile> usersDto = service.showUsers();
        request.setAttribute("users", usersDto);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-users.jsp").forward(request,response);
    }
}
