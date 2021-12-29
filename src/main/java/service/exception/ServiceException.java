package service.exception;

import javax.ejb.ApplicationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationException
public abstract class ServiceException extends RuntimeException{
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
