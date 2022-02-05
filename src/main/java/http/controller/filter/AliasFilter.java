package http.controller.filter;

import service.dto.CurrentUser;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@WebFilter("*")
public class AliasFilter extends HttpFilter {
    @Inject CurrentUser currentUser;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requri;
        try {
            requri = new URI(req.getRequestURI())
                    .normalize() //rimuove slash multipli e simili
                    .toString()
                    .substring(req.getContextPath().length() +1 );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            chain.doFilter(req, res);
            return;
        }
        String[] splits = requri.split("/+");
        switch(splits[0]){
            case "me":{
                if(currentUser.isLoggedIn()){
                    req.getRequestDispatcher("/user?name=" + currentUser.getUsername() + '&' + req.getQueryString())
                            .forward(req, res);
                    return;
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            case "popular": {
                req.getRequestDispatcher("/home?view=popular" + '&' + req.getQueryString()).forward(req, res);
                return;
            }
            case "feed":{
                req.getRequestDispatcher("/home?view=following" + '&' + req.getQueryString()).forward(req, res);
                return;
            }
            case "s":{
                if(splits.length >= 2) {
                    req.getRequestDispatcher("/s?section=" + splits[1] + '&' + req.getQueryString()).forward(req, res);
                    return;
                }
                break;
            }
            case "u": {
                if (splits.length >= 2) {
                    req.getRequestDispatcher("/user?name=" + splits[1] + '&' + req.getQueryString()).forward(req, res);
                    return;
                }
                break;
            }
            case "post":{
                if(splits.length >= 2){
                    req.getRequestDispatcher("/post?id=" + splits[1] + '&' + req.getQueryString()).forward(req, res);
                    return;
                }
                break;
            }
        }

        chain.doFilter(req, res);
    }
}
