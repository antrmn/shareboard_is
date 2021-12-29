package service.interceptor;

import service.Service;
import service.exception.BadRequestException;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.stream.Collectors;

@Priority(Interceptor.Priority.APPLICATION)
@Interceptor
@Service
public class ConstraintViolationInterceptor implements Serializable {

    @AroundInvoke
    public Object handle(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (javax.validation.ConstraintViolationException cve){
            throw new BadRequestException(
                    cve.getConstraintViolations().
                            stream().
                            map(ConstraintViolation::getMessage)
                            .collect(Collectors.toUnmodifiableList())
            );
        }
    }

}

