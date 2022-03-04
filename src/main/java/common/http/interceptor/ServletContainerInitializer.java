package common.http.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * Classe chiamata nella fase di start-up del servlet container. Vedi JSR 369 sec 8.2.4
 * @see ServletContainerInitializer
 */
@HandlesTypes(ServletInterceptor.class)
class ServletContainerInitializer implements javax.servlet.ServletContainerInitializer {
    /**
     * @param set L'insieme delle classi che rispettano i criteri imposti dall'annotazione {@link HandlesTypes}
     * @param servletContext Il servlet context della webapp
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) {
        //noinspection unchecked
        set.stream().map(x -> (Class<? extends ServletInterceptor<?>>) x).forEach(ServletInterceptorFactory::register);
    }
}
