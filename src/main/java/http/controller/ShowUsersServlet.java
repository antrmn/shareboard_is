package http.controller;

import service.UserService;
import service.dto.UserIdentityDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/showusers")
public class ShowUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserIdentityDTO> usersDto = new UserService().showUsers();
        request.setAttribute("users", usersDto);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-users.jsp").forward(request,response);
    }
}
