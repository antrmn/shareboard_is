package http.controller;

import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
@FormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue(loginPage = "/loginshow"))
public class LoginSubmit extends HttpServlet {
    @Inject
    SecurityContext securityContext;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession(true).invalidate();
        req.logout();

        System.out.println(securityContext.getClass().getName());
        Credential credential = new UsernamePasswordCredential(req.getParameter("username"), new Password(req.getParameter("password")));
        AuthenticationParameters auth = AuthenticationParameters.withParams().credential(credential).newAuthentication(true);
        AuthenticationStatus i = securityContext.authenticate(req, resp, auth);

        resp.sendRedirect("protected");
    }
}
