package service.auth;

import javax.ejb.ApplicationException;
import java.time.Instant;

@ApplicationException(rollback = true)
public class BannedUserException extends RuntimeException{
    private Instant duration;

    public Instant getDuration() {
        return duration;
    }

    public BannedUserException() {
    }

    public BannedUserException(Instant duration){

    }
}
