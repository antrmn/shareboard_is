package http.util.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;


public abstract class ServletInterceptor<A extends Annotation>{
    protected abstract void init (A annotation);

    public abstract void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next)
            throws ServletException, IOException;

    public int priority(){
        return Integer.MAX_VALUE/2;
    }
}