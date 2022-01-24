package http.controller;

import http.util.ParameterConverter;
import service.PostService;
import service.SectionService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet("/newpost")
@MultipartConfig
public class NewPostServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private PostService service;
    @Inject private SectionService sectionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/section/create-post.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            if(picture != null && picture.getSize() < 5 * 1024 * 1024) {
                BufferedInputStream buff = new BufferedInputStream(picture.getInputStream());
                newPostId = service.newPost(title,buff, picture.getSize(), sectionName);
            }else{
                throw new IllegalArgumentException("Il file non deve superare i 5MB");
            }
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + newPostId); //potrebbe mostrare postId = 0?
    }
}
