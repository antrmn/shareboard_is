package usecase.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@AuthenticationRequired
@Priority(Interceptor.Priority.APPLICATION+1)
public class AuthenticationRequiredInterceptor {
    @Inject private CurrentUser currentUser;

    @AroundInvoke
    public Object checkAdmin(InvocationContext invocationContext) throws Exception{
        if(!currentUser.isLoggedIn()){
            throw new AuthenticationRequiredException();
        }
        return invocationContext.proceed();
    }
}
