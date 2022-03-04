package usecase.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationConstraints {
    enum Types {
        REQUIRE_AUTHENTICATION, DENY_BANNED_USERS, ADMINS_ONLY;
    }

    Types[] value();
}
