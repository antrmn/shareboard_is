package http.controller.interceptor;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;


public abstract class ServletInterceptor<A extends Annotation>{

    protected abstract void init(A annotation);

    public abstract void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next)
            throws ServletException, IOException;

}
