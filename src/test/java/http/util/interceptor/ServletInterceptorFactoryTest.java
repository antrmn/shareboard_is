package http.util.interceptor;

import http.controller.interceptor.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.*;


@JSONError
@ForwardOnError("test")
@AuthorizationConstraints(AuthorizationConstraints.Types.ADMINS_ONLY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServletInterceptorTest {

    //utility shorthand method
    private Annotation getAnnotation(Class<? extends Annotation> annotationType){
        return this.getClass().getAnnotation(annotationType);
    }



    @BeforeAll
    void registerInterceptors(){
        try{
            ServletInterceptorFactory.register(JSONErrorInterceptor.class);
            ServletInterceptorFactory.register(ForwardOnErrorInterceptor.class);
            ServletInterceptorFactory.register(AuthorizationConstraintsInterceptor.class);
        } catch(IllegalArgumentException e){
            //noop
        }
    }

    @Test
    @JSONError
    void instantiate() {
        Assertions.assertNotNull(ServletInterceptorFactory.instantiate(getAnnotation(JSONError.class)));
        Assertions.assertNull(ServletInterceptorFactory.instantiate(getAnnotation(TestInstance.class)));
    }

    @NoZeroArgConstructor
    @Test
    void instantiateReflectiveIllegalOperation(){
        ServletInterceptorFactory.register(NoZeroArgConstructorInterceptor.class);
        Annotation annotation = NoZeroArgConstructorAnnotationLiteral.get();
        Assertions.assertThrows(RuntimeException.class,() -> ServletInterceptorFactory.instantiate(annotation));
    }

    @Test
    void registerAbstractClass() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ServletInterceptorFactory.register(AbstractInterceptor.class));
    }

    @Test
    void registerAlreadyRegisteredClass(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ServletInterceptorFactory.register(JSONErrorInterceptor.class));
    }



    /* Classi definite per i test */

    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface NoZeroArgConstructor{

    }

    @NoZeroArgConstructor
    private static class NoZeroArgConstructorAnnotationLiteral{
        private static NoZeroArgConstructor get(){
            return NoZeroArgConstructorAnnotationLiteral.class.getAnnotation(NoZeroArgConstructor.class);
        }
    }

    private static class NoZeroArgConstructorInterceptor extends ServletInterceptor<NoZeroArgConstructor>{
        public NoZeroArgConstructorInterceptor(int arg){}

        @Override
        protected void init(NoZeroArgConstructor annotation) {

        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {

        }
    }

    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface AbstractInterceptorAnnotation{
    }

    private abstract static class AbstractInterceptor extends ServletInterceptor<AbstractInterceptorAnnotation>{
    }

    private @interface GenericInterceptorAnnotation{
    }

    private static class GenericInterceptor extends ServletInterceptor<GenericInterceptorAnnotation>{

        @Override
        protected void init(GenericInterceptorAnnotation annotation) {

        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {

        }
    }
}