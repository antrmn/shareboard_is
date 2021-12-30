package http.controller;

import service.dto.LoggedInUser;

import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.Principal;

@WebServlet("/protected")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"user","admin"}))
public class ProtectedResource extends HttpServlet {

    @Inject protected Principal principal;
    @Inject
    LoggedInUser linu;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(principal.getName());
        System.out.println(linu);
    }
}
