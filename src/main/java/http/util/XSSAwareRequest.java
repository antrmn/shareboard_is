package http.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

public class XSSAwareRequest extends HttpServletRequestWrapper {
    public XSSAwareRequest(HttpServletRequest req){
        super(req);
    }

    private static final Map<Character, String> ESCAPES =
            Map.of( '<', "&lt;",
                    '>', "&gt;",
                    '&', "&amp;",
                    '\\',"&#039;",
                    '"', "&#034");

    private String escape(String txt){
        for(Character c : ESCAPES.keySet()){
            txt = txt.replaceAll(c.toString(), ESCAPES.get(c)); //TODO: optimization
        }
        return txt;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return value == null ? null : escape(value);
    }
}
