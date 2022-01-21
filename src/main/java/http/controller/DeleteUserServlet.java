package http.controller;

import service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/deleteuser")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _userId = request.getParameter("userId");
        int userId = 0;
        if(_userId != null && _userId.matches("\\d*")){
            userId = Integer.parseInt(_userId);
        }
        new UserService().delete(userId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showusers");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
