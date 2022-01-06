package http.controller.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletInterceptorChain {
    void handle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
