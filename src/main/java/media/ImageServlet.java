package media;

import common.http.interceptor.InterceptableServlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.MappingMatch;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@WebServlet("/image/*")
class ImageServlet extends InterceptableServlet {
    @Inject ImageService imageService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletMapping httpServletMapping = req.getHttpServletMapping();
        if(httpServletMapping.getMappingMatch().equals(MappingMatch.PATH)){
            String filename = httpServletMapping.getMatchValue();
            try(InputStream stream = imageService.getImage(filename)){
                if(stream == null){
                    resp.setStatus(404);
                    return;
                }
                String mime = URLConnection.guessContentTypeFromStream(stream);
                resp.setContentType(mime);
                stream.transferTo(resp.getOutputStream());
                resp.getOutputStream().flush();
            }
        } else {
            resp.setStatus(404);
        }
    }
}
