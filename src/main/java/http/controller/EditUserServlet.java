package http.controller;

import persistence.model.User;
import service.UserService;
import service.dto.UserEditPage;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet("/edituser")
@MultipartConfig
public class EditUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _userId = request.getParameter("id");
        int userId = 0;
        if(_userId != null && _userId.matches("\\d*")){
            userId = Integer.parseInt(_userId);
        }
        User user = new UserService().getUser(userId);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _userId = request.getParameter("id");
        int userId = 0;
        if(_userId != null && _userId.matches("\\d*")){
            userId = Integer.parseInt(_userId);
        }

        String email = request.getParameter("email");
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        String pass = request.getParameter("pass");
        String pass2 = request.getParameter("pass2"); //todo: verificare l'uguaglianza delle password

        BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
        UserEditPage userEditPage = new UserEditPage(userId,description,email,buffPicture,pass);
        UserService service = new UserService();
        service.edit(userEditPage,userId);

        response.sendRedirect(request.getContextPath() + "/u/" + service.getUsernameById(userId));
    }
}
