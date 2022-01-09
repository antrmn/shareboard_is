package http.controller;

import lombok.extern.java.Log;
import service.dto.LoggedInUser;

import javax.inject.Inject;

import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import static javax.security.enterprise.AuthenticationStatus.NOT_DONE;
import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.*;


@WebServlet("/doLogin")
@ServletSecurity(httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = {"**"})
})
public class DoLoginServlet extends HttpServlet {
    @Inject SecurityContext securityContext;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username") == null ? "" : req.getParameter("username");
        String password = req.getParameter("pass") == null ? "" : req.getParameter("pass");

        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        AuthenticationStatus authenticate =
                securityContext.authenticate(req, resp,
                        withParams().credential(credential));
        if(AuthenticationStatus.SEND_FAILURE.equals(authenticate)){
            req.getRequestDispatcher("/WEB-INF/views/login.jsp?error").forward(req, resp);
        }
    }
}
