package http.util.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Questa classe rappresenta un <i>factory</i> per la creazione di oggetti che estendono la classe astratta
 * {@link ServletInterceptor}.<br/>
 *
 * I metodi di questa classe consentono la registrazione di classi ammissibili per l'istanziazione
 * e l'istanziazione di questi ultimi.
 *
 */
public final class ServletInterceptorFactory {
    private ServletInterceptorFactory(){}

    private static final
    ConcurrentMap<Class<? extends Annotation>, Class<? extends ServletInterceptor<?>>> classes =
            new ConcurrentHashMap<>();

    /**
     * Restituisce un'istanza di {@link ServletInterceptor} associata all'annotazione passata come parametro.<br/>
     * La classe concreta dell'oggetto restituito corrisponde a una sottoclasse di {@link ServletInterceptor} registrata
     * nella classe factory per mezzo di una previa chiamata al metodo {@link ServletInterceptorFactory#register(Class)}.<br/>
     *
     * Per poter essere istanziata dal factory, la classe concreta in questione deve possedere un costruttore vuoto pubblico.
     *
     *
     * @param annotation Il tipo di annotazione associato alla classe interceptor da istanziare
     * @param <T> Tipo parametrizzato corrispondente al tipo dell'annotazione
     * @return Un'istanza di {@link ServletInterceptor} associata all'annotazione passata come parametro
     *
     * @throws RuntimeException Se la creazione dell'interceptor fallisce
     */
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

    /**
     * Registra una sottoclasse di {@link ServletInterceptor} per una seguente istanziazione
     *
     * @param interceptorClass La classe da registrare
     * @throws IllegalArgumentException se si tenta di registrare una classe astratta oppure
     * se si tenta di registrare una classe con lo stesso tipo parametrizzato
     */
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