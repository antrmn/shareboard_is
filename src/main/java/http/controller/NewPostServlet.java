package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.controller.interceptor.ForwardOnError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import service.PostService;
import service.SectionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

import static http.controller.interceptor.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet("/newpost")
@MultipartConfig
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
public class NewPostServlet extends InterceptableServlet {
    @Inject private PostService service;
    @Inject private SectionService sectionService;

    private static final String NEW_POST_PAGE = "/WEB-INF/views/section/create-post.jsp";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(NEW_POST_PAGE).forward(request, response);
    }

    @Override
    @ForwardOnError("/WEB-INF/views/section/create-post.jsp")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int sectionId = converter.getIntParameter("section").orElse(0);
        String sectionName = sectionService.showSection(sectionId).getName();

        String title = request.getParameter("title");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        Part picture = request.getPart("picture");

        int newPostId;
        if(type.equalsIgnoreCase("text")){
            newPostId = service.newPost(title,content,sectionName);
        }else{
            if(picture != null && picture.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("Il file non deve superare i 5MB");
            }

            if(picture != null && picture.getSize() == 0) {
                throw new IllegalArgumentException("Immagine mancante");
            }
            BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
            newPostId = service.newPost(title,buffPicture, picture.getSize(), sectionName);

        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + newPostId); //potrebbe mostrare postId = 0?
    }
}
