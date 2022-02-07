package http.controller;

import service.ImageService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
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
