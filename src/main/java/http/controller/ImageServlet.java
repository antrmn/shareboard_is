package http.controller;

import persistence.repo.BinaryContentRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
    @Inject
    BinaryContentRepository binaryContentRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletMapping httpServletMapping = req.getHttpServletMapping();
        if(httpServletMapping.getMappingMatch().equals(MappingMatch.PATH)){
            String filename = httpServletMapping.getMatchValue();
            try(InputStream stream = new BufferedInputStream(binaryContentRepository.get(filename))){
                String mimetype = URLConnection.guessContentTypeFromStream(stream);
                if(mimetype == null || !mimetype.startsWith("image/")){
                    resp.sendError(404);
                }
                resp.setContentType(mimetype);
                stream.transferTo(resp.getOutputStream());
                resp.getOutputStream().flush();
            }
        } else {
            resp.sendError(404);
        }
    }
}
