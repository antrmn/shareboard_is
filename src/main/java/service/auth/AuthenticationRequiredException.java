package service.auth;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AuthenticationRequiredException extends RuntimeException {
    public AuthenticationRequiredException() {
    }

    public AuthenticationRequiredException(String message) {
        super(message);
    }

    public AuthenticationRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationRequiredException(Throwable cause) {
        super(cause);
    }
}
