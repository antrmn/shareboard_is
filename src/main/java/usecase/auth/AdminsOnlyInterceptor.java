package usecase.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Classe interceptor per verificare lo stato di admin di un utente loggato.
 */
@Interceptor
@AdminsOnly
@Priority(Interceptor.Priority.APPLICATION+3)
public class AdminsOnlyInterceptor {
    @Inject private CurrentUser currentUser;

    /**
     * Controlla se l'utente loggato è un admin.
     * @param invocationContext
     * @return Oggetto che autorizza a procedere con le operazioni.
     * @throws AuthorizationException
     */
    @AroundInvoke
    public Object checkAdmin(InvocationContext invocationContext) throws Exception{
        if(!currentUser.isLoggedIn() || !currentUser.isAdmin()){
            throw new AuthorizationException();
        }
        return invocationContext.proceed();
    }
}
