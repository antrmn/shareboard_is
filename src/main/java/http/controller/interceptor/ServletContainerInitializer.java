package http.controller.interceptor;

import http.controller.interceptor.ServletInterceptor;
import http.controller.interceptor.ServletInterceptorFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

@HandlesTypes(ServletInterceptor.class)
public class ServletContainerInitializer implements javax.servlet.ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) {
        //noinspection unchecked
        set.stream().map(x -> (Class<? extends ServletInterceptor<?>>) x).forEach(ServletInterceptorFactory::register);
    }
}
