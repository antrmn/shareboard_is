package http.controller.interceptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;

@ApplicationScoped
public class MyProducer {
    public <T extends Annotation> ServletInterceptor<T> of(T annotation) {
        ServletInterceptor<JSONError> jsonErrorServletInterceptor = CDI.current().select(new TypeLiteral<ServletInterceptor<JSONError>>() {
        }, Any.Literal.INSTANCE).get();
        jsonErrorServletInterceptor.init((JSONError) annotation);
        return (ServletInterceptor<T>) jsonErrorServletInterceptor;
    }

}
