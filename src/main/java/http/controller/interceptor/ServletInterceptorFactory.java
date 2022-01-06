package http.controller.interceptor;

import java.util.Set;

public class ServletInterceptorFactory <A> {
    private static ServletInterceptorFactory factory = null;

    private ServletInterceptorFactory() { }

    public static ServletInterceptorFactory getInstance() {
        if (factory == null) {
            factory = new ServletInterceptorFactory();
        }
        return factory;
    }

    public <X extends ServletInterceptor> X getInstance(Class<X> clazz){
        if(clazz.equals(ServletInterceptor.class)){

        }
    }
}
