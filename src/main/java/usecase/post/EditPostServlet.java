package usecase.post;

import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

/**
 * Classe che permette di modificare un post.
 */
@WebServlet("/editpost")
@MultipartConfig
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class EditPostServlet extends InterceptableServlet {
    //Funzionalità disabilitata

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Funzionalità disabilitata temporaneamente");
    }


    /*@Inject private PostService service;

    private static final String EDIT_POST_PAGE = "/WEB-INF/views/section/edit-post.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check necessario?
        ParameterConverter converter = new ParameterConverter(request);
        int postId = converter.getIntParameter("id").orElse(0);
        PostPage post =  service.getPost(postId);
        request.setAttribute("post", post);
        request.getRequestDispatcher(EDIT_POST_PAGE).forward(request, response);
    }

    @Override
    @ForwardOnError(EDIT_POST_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
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
    */
}
