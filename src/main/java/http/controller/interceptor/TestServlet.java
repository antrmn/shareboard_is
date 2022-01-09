package http.controller.interceptor;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/haha")
public class TestServlet extends HttpServlet {
    @Inject MyProducer myProducer;

    @Override
    @JSONError(lol = "Eccolooooooooooooo il pramatero")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Method a = null;
        try {
            a = this.getClass().getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
            JSONError annotation = a.getAnnotation(JSONError.class);
            myProducer.of(annotation).handle(req,resp,this::doPost);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("sto nel post");
    }
}
