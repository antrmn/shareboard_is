package http.controller.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class InterceptableServlet extends HttpServlet {
    private static final Map<String, String> methods =
            Map.of(
                    "GET",     "doGet",
                    "POST",    "doPost",
                    "PUT",     "doPut",
                    "TRACE",   "doTrace",
                    "OPTIONS", "doOptions",
                    "DELETE",  "doDelete",
                    "HEAD",    "doHead"
            );

    private final Map<String, HttpServletBiConsumer> chains = new HashMap<>();
    private boolean chainInitialized = false;

    private void initChain(){
        methods.forEach((httpMethod,servletMethod)
                -> chains.put( httpMethod, buildChain(getInterceptors(servletMethod), this::superService)) );
        chainInitialized = true;
    }

    private ServletInterceptor<?>[] getInterceptors(String methodName) {
        //Not a "doX" method
        if(!methods.containsValue(methodName)){
            throw new IllegalArgumentException();
        }

        Method method;
        try {
            method = this.getClass()
                    .getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            //method has not been overriden by this class.
            //return empty array
            return new ServletInterceptor[]{};
        }

        //get annotations and their respective interceptor
        return Arrays.stream(method.getAnnotations())
                .map(ServletInterceptorFactory::instantiate)
                .filter(Objects::nonNull)
                .toArray(ServletInterceptor[]::new);
    }

    private HttpServletBiConsumer buildChain(ServletInterceptor<?>[] interceptors, HttpServletBiConsumer target){
        interceptors = interceptors == null ? new ServletInterceptor[]{} : interceptors;

        HttpServletBiConsumer current = target;
        for(int i = interceptors.length-1; i >= 0; i--){
            ServletInterceptor<?> interceptor = interceptors[i];

            HttpServletBiConsumer next = current;
            current = (req, res) -> interceptor.handle(req, res, next);
        }
        return current;
    }

    //unambiguous super.service (there are 2 service methods! java lambdas hate that.)
    private void superService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!chainInitialized)
            initChain();
        chains.get(req.getMethod()).handle(req,resp);
    }

    @Override
    public final void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        service((HttpServletRequest) req, (HttpServletResponse) res);
    }

}