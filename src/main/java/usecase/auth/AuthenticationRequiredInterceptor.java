package usecase.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Classe interceptor per confermare l'autenticazione
 */
@Interceptor
@AuthenticationRequired
@Priority(Interceptor.Priority.APPLICATION+1)
public class AuthenticationRequiredInterceptor {
    @Inject private CurrentUser currentUser;

    /**
     * Verifica l'autenticazione
     * @param invocationContext
     * @return Oggetto che conferma la possibilità di procedere.
     * @throws AuthenticationRequiredException
     */
    @AroundInvoke
    public Object checkAdmin(InvocationContext invocationContext) throws Exception{
        if(!currentUser.isLoggedIn()){
            throw new AuthenticationRequiredException();
        }
        return invocationContext.proceed();
    }
}
