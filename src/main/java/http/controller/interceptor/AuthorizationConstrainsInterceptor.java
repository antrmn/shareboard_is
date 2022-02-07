package http.controller.interceptor;

import http.util.interceptor.HttpServletBiConsumer;
import http.util.interceptor.ServletInterceptor;
import service.auth.AuthenticationRequiredException;
import service.auth.AuthorizationException;
import service.auth.BannedUserException;
import service.dto.CurrentUser;

import javax.enterprise.inject.literal.NamedLiteral;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import static http.controller.interceptor.AuthorizationConstraints.Types.*;

public class AuthorizationConstrainsInterceptor extends ServletInterceptor<AuthorizationConstraints> {

    private Set<AuthorizationConstraints.Types> authorizationConstraintTypes;

    @Override
    protected void init(AuthorizationConstraints annotation) {
        authorizationConstraintTypes = EnumSet.noneOf(AuthorizationConstraints.Types.class);
        authorizationConstraintTypes.addAll(Arrays.asList(annotation.value()));
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
        //equivalente di @Inject CurrentUser in forma programmatica
        CurrentUser currentUser = CDI.current().select(CurrentUser.class, NamedLiteral.of("currentUser")).get();
        if(authorizationConstraintTypes.contains(ADMINS_ONLY)
                || authorizationConstraintTypes.contains(REQUIRE_AUTHENTICATION)){
            //controlla se l'utente è autenticato
            if(!currentUser.isLoggedIn()){
                throw new AuthenticationRequiredException();
            }
        }

        if(authorizationConstraintTypes.contains(ADMINS_ONLY)){
            if(!currentUser.isAdmin()){
                throw new AuthorizationException();
            }
        }

        if(authorizationConstraintTypes.contains(DENY_BANNED_USERS)){
            if(currentUser.getBanDuration() != null){
                throw new BannedUserException();
            }
        }

        next.handle(req,resp);
    }

    @Override
    public int priority() {
        return super.priority();
    }
}
