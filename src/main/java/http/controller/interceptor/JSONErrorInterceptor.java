package http.controller.interceptor;

import service.exception.ServiceException;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.interceptor.Interceptor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Dependent
@Default
public class JSONErrorInterceptor extends ServletInterceptor<JSONError>{
    private String lol;

    @Override
    protected void init(JSONError annotation) {
        this.lol = annotation.lol();
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next) throws ServletException, IOException {
        System.out.println("sono io!!!!!" + lol);
        try{
            next.handle(req,resp);
        } catch(ServiceException e){
            //produce
        }
    }
}
