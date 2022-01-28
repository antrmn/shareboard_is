package http.controller;

import http.controller.interceptor.ForwardOnError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import persistence.model.Post;
import service.PostService;
import service.dto.PostEditDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet("/editpost")
@MultipartConfig
public class EditPostServlet extends InterceptableServlet {
    @Inject private ParameterConverter converter;
    @Inject private PostService service;

    private static final String EDIT_POST_PAGE = "/WEB-INF/views/section/edit-post.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check necessario?
        request.getRequestDispatcher(EDIT_POST_PAGE).forward(request, response);
    }

    @Override
    @ForwardOnError(EDIT_POST_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String type = request.getParameter("type");
        Post.Type postType = type != null && type.equalsIgnoreCase("text") ? Post.Type.TEXT : Post.Type.IMG;
        Part picture = request.getPart("picture");
        String content = request.getParameter("content");

        PostEditDTO postToEdit = new PostEditDTO(title,content,postType);
        //in caso di immagine il content sarà null infatti non sarà utilizzato nel servizio EditPostIMG

        int postId = converter.getIntParameter("id").orElse(0);
        if(postType == Post.Type.TEXT){
            service.editPost(postToEdit,postId);
        }else{
            BufferedInputStream buff = new BufferedInputStream(picture.getInputStream());
            service.editPost(postToEdit,postId,buff);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + postId);
    }
}
