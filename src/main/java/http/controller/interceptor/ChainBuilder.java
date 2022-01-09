package http.controller.interceptor;

public final class ChainBuilder {
    private ChainBuilder(){}

    public static HttpServletBiConsumer make(Class<? extends ServletInterceptor>[] interceptors, HttpServletBiConsumer target){
        HttpServletBiConsumer current = target;
        for(int i = interceptors.length-1; i >= 0; i--){
          //  ServletInterceptor inter = ServletInterceptor.of(interceptors[i]);

            HttpServletBiConsumer next = current;
         //   current = (req, res) -> inter.handle(req, res, next);
        }
        return current;
    }
}
