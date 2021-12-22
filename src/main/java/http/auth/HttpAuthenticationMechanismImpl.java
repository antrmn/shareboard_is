package http.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.CallerOnlyCredential;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class HttpAuthenticationMechanismImpl implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler storeHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest req, HttpServletResponse res,
                                                HttpMessageContext ctx) {
        CredentialValidationResult validationResult = null;
        if(ctx.isAuthenticationRequest()){
            Credential credential = ctx.getAuthParameters().getCredential();
            validationResult = storeHandler.validate(credential);

            if(validationResult.getStatus() == CredentialValidationResult.Status.VALID){
                req.getSession(true).setAttribute("loggedUsername",validationResult.getCallerPrincipal().getName());
            }
            return ctx.notifyContainerAboutLogin(validationResult);
        } else if(req.getSession(false) != null) {
            HttpSession session = req.getSession(true);
            String username = (String) session.getAttribute("loggedUsername");
            if(username != null){
                validationResult = storeHandler.validate(new CallerOnlyCredential(username));
                if(validationResult.getStatus() == CredentialValidationResult.Status.INVALID){
                    session.invalidate();
                    validationResult = null;
                }
            }
        } else if (ctx.isProtected()) {
            return ctx.redirect(getLoginPageUrl(req));
        }

        if (validationResult != null) {
            switch (validationResult.getStatus()) {
                case VALID:
                    return ctx.notifyContainerAboutLogin(validationResult);
                case INVALID:
                    res.setStatus(401);
                    writeJsonError(res, 401, "Authorization failed");
                    return ctx.notifyContainerAboutLogin(validationResult);
                case NOT_VALIDATED:
                default:
                    res.setStatus(503);
                    writeJsonError(res, 503, "No connection to DB");
                    return ctx.notifyContainerAboutLogin(validationResult);
            }
        }
        return ctx.doNothing();
    }

    private String getLoginPageUrl(HttpServletRequest request) {
        return "login?go=" + URLEncoder.encode(request.getRequestURI(), StandardCharsets.UTF_8);
    }

    private void writeJsonError(HttpServletResponse response, int status, String errorMessage) {
        try {
            Json.createGenerator(response.getOutputStream())
                    .writeStartObject()
                    .writeStartObject("error")
                    .write("httpStatus", status)
                    .write("errorMessage", errorMessage)
                    .writeEnd()
                    .writeEnd()
                    .close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}