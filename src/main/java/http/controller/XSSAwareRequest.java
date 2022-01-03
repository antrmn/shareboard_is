package http.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class XSSAwareRequest extends HttpServletRequestWrapper {
    protected XSSAwareRequest(HttpServletRequest req){
        super(req);
    }

    private static final Map<Character, String> ESCAPES =
            Map.of( '<', "&lt;",
                    '>', "&gt;",
                    '&', "&amp;",
                    '\\',"&#039;",
                    '"', "&#034");

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(value == null)
            return null;

        for(Character c : ESCAPES.keySet()){
            value = value.replaceAll(c.toString(), ESCAPES.get(c));
        }
        return value;
    }

    //todo: getparametermap, separate escape method
}
