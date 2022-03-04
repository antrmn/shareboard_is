package usecase.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@DenyBannedUsers
@Priority(Interceptor.Priority.APPLICATION+2)
class DenyBannedUsersInterceptor {
    @Inject private CurrentUser currentUser;

    @AroundInvoke
    public Object checkAdmin(InvocationContext invocationContext) throws Exception{
        if(currentUser.isLoggedIn() && currentUser.getBanDuration() != null){
            throw new BannedUserException(currentUser.getBanDuration());
        }
        return invocationContext.proceed();
    }
}
