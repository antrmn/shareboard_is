package http.controller.interceptors;

import http.controller.interceptor.HttpServletBiConsumer;
import http.controller.interceptor.ServletInterceptor;
import service.exception.BadRequestException;
import service.exception.ServiceException;

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
        }catch(BadRequestException e){
            //error as json
        }
    }
}
