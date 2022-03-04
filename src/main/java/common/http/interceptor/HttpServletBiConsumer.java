package common.http.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rapprsenta un'operazione che accetta un oggetto {@link javax.servlet.http.HttpServletRequest}, un oggetto {@link HttpServletResponse}
 * e può lanciare {@link ServletException} oppure {@link IOException}.</br>  
 * 
 * Questa è un'interfaccia funzionale il cui metodo funzionale è {@link HttpServletBiConsumer#handle(HttpServletRequest, HttpServletResponse)}
 */
@FunctionalInterface
public interface HttpServletBiConsumer {
    void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
