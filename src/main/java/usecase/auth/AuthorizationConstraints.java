package usecase.auth;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorizationConstraints {
    enum Types {
        REQUIRE_AUTHENTICATION, DENY_BANNED_USERS, ADMINS_ONLY;
    }

    Types[] value();
}
