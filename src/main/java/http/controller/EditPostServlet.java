package http.controller;

import persistence.model.Post;
import service.PostService;
import service.dto.PostEditDTO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
            service.EditPost(postToEdit,postId);
        }else{
            BufferedInputStream buff = new BufferedInputStream(picture.getInputStream());
            service.EditPost(postToEdit,postId,buff);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + postId);
    }
}
