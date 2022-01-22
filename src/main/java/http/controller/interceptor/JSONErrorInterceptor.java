package http.controller.interceptor;

import http.util.interceptor.HttpServletBiConsumer;
import http.util.interceptor.ServletInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JSONErrorInterceptor extends ServletInterceptor<JSONError> {

    @Override
    protected void init(JSONError annotation) {

    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
        try{
            next.handle(req,resp);
        }catch(Exception e){
            //error as json
        }
    }
}
