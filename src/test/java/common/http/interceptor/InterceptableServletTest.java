package common.http.interceptor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InterceptableServletTest {

    public List<Class<? extends ServletInterceptor<?>>> interceptors = List.of(HighPriorityInterceptor.class,
            LowPriorityInterceptor.class, MidPriorityInterceptor.class);



    @BeforeAll
    void registerInterceptors(){
        interceptors.forEach(ServletInterceptorFactory::register);
    }


    @Test
    void httpMethodNotRecognizedTest(){
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        InterceptableServlet servlet = new SampleInterceptableServlet();
        when(req.getMethod()).thenReturn("trololol");
        assertThrows(IllegalArgumentException.class,() -> servlet.service(req,resp));
    }

    @Test
    void interceptorsOrderTest() throws ServletException, IOException {
        {
            List<Integer> sequence = new ArrayList<>();
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            when(req.getAttribute(any())).thenReturn(sequence);
            InterceptableServlet servlet = new SampleInterceptableServlet();
            when(req.getMethod()).thenReturn("GET");
            HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
            servlet.service(req,resp);
            assertEquals(sequence,List.of(3,2,1,4));
        }
        {
            List<Integer> sequence = new ArrayList<>();
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            when(req.getAttribute(any())).thenReturn(sequence);
            InterceptableServlet servlet = new SampleInterceptableServlet();
            when(req.getMethod()).thenReturn("POST");
            HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
            servlet.service((ServletRequest) req,(ServletResponse) resp);
            assertEquals(sequence,List.of(2,5,1));
        }
        {
            List<Integer> sequence = new ArrayList<>();
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            when(req.getAttribute(any())).thenReturn(sequence);
            InterceptableServlet servlet = new SampleInterceptableServlet();
            when(req.getMethod()).thenReturn("PUT");
            HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
            assertThrows(NullPointerException.class, () -> servlet.service(req,resp));
            assertEquals(sequence,List.of());
        }
    }




    @LowPriority(1) //ordinamento stabile: a parità di priorità, annotazioni definite a livello di classe hanno la precedenza
    @MidPriority(2)
    private static class SampleInterceptableServlet extends InterceptableServlet{
        @Override
        @HighPriority(3)
        @LowPriority(4)
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }

        @MidPriority(5)
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        }
    }

    /* Low, Mid and High priority interceptors */

    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface LowPriority{
        int value();
    }

    private static class LowPriorityInterceptor extends ServletInterceptor<LowPriority>{
        public int value;
        public LowPriorityInterceptor(){

        }

        @Override
        protected void init(LowPriority annotation) {
            value = annotation.value();
        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
            List<Integer> sequence = (List<Integer>) req.getAttribute("test");
            sequence.add(this.value);
            next.handle(req,resp);
        }

        @Override
        public int priority() {
            return Integer.MAX_VALUE;
        }
    }

    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface MidPriority{
        int value();
    }

    private static class MidPriorityInterceptor extends ServletInterceptor<MidPriority>{
        public int value;
        public MidPriorityInterceptor(){

        }

        @Override
        protected void init(MidPriority annotation) {
            value = annotation.value();
        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
            List<Integer> sequence = (List<Integer>) req.getAttribute("test");
            sequence.add(this.value);
            next.handle(req,resp);
        }

        @Override
        public int priority() {
            return super.priority();
        }
    }

    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface HighPriority{
        int value();
    }

    private static class HighPriorityInterceptor extends ServletInterceptor<HighPriority>{
        public int value;
        public HighPriorityInterceptor(){

        }

        @Override
        protected void init(HighPriority annotation) {
            value = annotation.value();
        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
            List<Integer> sequence = (List<Integer>) req.getAttribute("test");
            sequence.add(this.value);
            next.handle(req,resp);
        }

        @Override
        public int priority() {
            return 0;
        }
    }


}