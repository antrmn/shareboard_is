package usecase.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@AdminsOnly
@Priority(Interceptor.Priority.APPLICATION+3)
public class AdminsOnlyInterceptor {
    @Inject private CurrentUser currentUser;

    @AroundInvoke
    public Object checkAdmin(InvocationContext invocationContext) throws Exception{
        if(!currentUser.isLoggedIn() || !currentUser.isAdmin()){
            throw new AuthorizationException();
        }
        return invocationContext.proceed();
    }
}
