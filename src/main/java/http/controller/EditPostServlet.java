package http.controller;

import persistence.model.Post;
import service.PostService;
import service.dto.PostEditDTO;

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
public class EditPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check necessario?
        request.getRequestDispatcher("/WEB-INF/views/section/edit-post.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostService service = new PostService();
        String title = request.getParameter("title");

        String type = request.getParameter("type"); //errore? se type non è text, sarà IMG di default
        Post.Type postType = type != null && type.equalsIgnoreCase("text") ? Post.Type.TEXT : Post.Type.IMG;
        Part picture = request.getPart("picture");
        String content = request.getParameter("content");

        PostEditDTO postToEdit = new PostEditDTO(title,content,postType);
        //in caso di immagine il content sarà null infatti non sarà utilizzato nel servizio EditPostIMG

        String _postId = request.getParameter("id");
        int postId = 0;
        if(_postId != null && _postId.matches("\\d*")){
            postId = Integer.parseInt(_postId);
        }

        if(type.equalsIgnoreCase("text")){
            service.editPost(postToEdit,postId);
        }else{
            BufferedInputStream buff = new BufferedInputStream(picture.getInputStream());
            service.editPost(postToEdit,postId,buff);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + postId);
    }
}
