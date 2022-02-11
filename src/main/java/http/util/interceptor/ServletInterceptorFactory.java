package http.util.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ServletInterceptorFactory {
    private ServletInterceptorFactory(){}

    private static final
    ConcurrentMap<Class<? extends Annotation>, Class<? extends ServletInterceptor<?>>> classes =
            new ConcurrentHashMap<>();

    public static <T extends Annotation> ServletInterceptor<T> instantiate(T annotation){
        Class<? extends ServletInterceptor<?>> clazz = ServletInterceptorFactory.classes.get(annotation.annotationType());
        if(clazz == null)
            return null;

        ServletInterceptor<T> interceptor;
        try {
            //noinspection unchecked
            interceptor = (ServletInterceptor<T>) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        interceptor.init(annotation);
        return interceptor;
    }

    public static void register(Class<? extends ServletInterceptor<? extends Annotation>> interceptorClass){
        if(Modifier.isAbstract(interceptorClass.getModifiers())){
            throw new IllegalArgumentException("Interceptor class must be concrete");
        }

        //get ServletInterceptor.class immediate subclass
        Class<?> clazz = interceptorClass;
        while(clazz.getSuperclass() != ServletInterceptor.class){
            clazz = clazz.getSuperclass();
        }
        //get ServletInterceptor.class as ParameterizedType object
        ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();

        @SuppressWarnings("unchecked")
        Class<? extends Annotation> actualTypeArgument =
                (Class<? extends Annotation>) genericSuperclass.getActualTypeArguments()[0];

        Class<?> oldValue = classes.putIfAbsent(actualTypeArgument, interceptorClass);
        if(oldValue != null){
            throw new IllegalArgumentException("There already is a registered Servlet Interceptor with annotation "
                    + actualTypeArgument.getName());
        }
    }
}