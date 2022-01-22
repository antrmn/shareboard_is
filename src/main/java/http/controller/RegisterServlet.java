package http.controller;

import service.PostService;
import service.UserService;
import service.dto.CurrentUser;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Objects;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Inject private UserService userService;
    @Inject private CurrentUser currentUser;
    @Inject private PostService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            service.newPost("", "", "");
        } catch (ConstraintViolationException e){
            e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
        }
        System.out.println(CDI.current().getClass().getName());
        if(currentUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(currentUser.isLoggedIn()){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String password = req.getParameter("pass");
        String confirmPassword = req.getParameter("pass2");

        if(!Objects.equals(password, confirmPassword)){
            throw new IllegalArgumentException("Le password devono coincidere");
        }
        userService.newUser(email, username, password);
        resp.sendRedirect(req.getContextPath());
    }
}
