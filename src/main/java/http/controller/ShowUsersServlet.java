package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import service.UserService;
import service.dto.UserIdentityDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static http.controller.interceptor.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/showusers")
@AuthorizationConstraints(ADMINS_ONLY)
public class ShowUsersServlet extends HttpServlet {

    @Inject private UserService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserIdentityDTO> usersDto = service.showUsers();
        request.setAttribute("users", usersDto);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-users.jsp").forward(request,response);
    }
}
