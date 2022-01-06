package http.controller.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

abstract class ServletInterceptor implements ServletInterceptorChain {
    private static final ServletInterceptorChain NOOP = (req, resp) -> {};

    private final ServletInterceptorChain chain;

    public static <T> ServletInterceptor of(Class<T> clazz) {
        try {
            return (ServletInterceptor) clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e){
            throw new RuntimeException(e);
        }
    }

    public ServletInterceptor(ServletInterceptorChain chain) {
        this.chain = chain != null ? chain : NOOP;
    }

    public final void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp, chain);
    }

    public abstract void handle(HttpServletRequest req, HttpServletResponse resp, ServletInterceptorChain chain)
            throws ServletException, IOException;

}
