package http.controller;

import service.UserService;
import service.dto.UserIdentityDTO;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/showusers")
public class ShowUsersServlet extends HttpServlet {

    @Inject private UserService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserIdentityDTO> usersDto = service.showUsers();
        request.setAttribute("users", usersDto);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-users.jsp").forward(request,response);
    }
}
