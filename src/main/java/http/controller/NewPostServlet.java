package http.controller;

import service.PostService;
import service.SectionService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet(name = "NewPostServlet", value = "/NewPostServlet")
public class NewPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/section/create-post.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _sectionId = request.getParameter("section");
        int sectionId = _sectionId != null && _sectionId.matches("\\d*") ? Integer.parseInt(_sectionId) : 0;
        String sectionName = new SectionService().ShowSection(sectionId).getName();

        String title = request.getParameter("title");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        Part picture = request.getPart("picture");
        PostService servicePost = new PostService();

        int newPostId = 0;
        if(type.equalsIgnoreCase("text")){
            newPostId = servicePost.newPost(title,content,sectionName);
        }else{
            if(picture != null && picture.getSize() < 5 * 1024 * 1024) {
                BufferedInputStream buff = new BufferedInputStream(picture.getInputStream());
                newPostId = servicePost.newPost(title,buff, picture.getSize(), sectionName);
            }else{
                //gestire errore file > 5MB?
            }
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + newPostId); //potrebbe mostrare postId = 0?
    }
}
