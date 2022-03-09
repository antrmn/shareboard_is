package usecase.post;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.comment.CommentDTO;
import usecase.comment.CommentService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servlet per la visualizzazione di un post e i relativi commenti.
 */
@WebServlet("/post")
class PostServlet extends InterceptableServlet {

    @Inject private PostService postService;
    @Inject private CommentService service;

    @Override
    public void init() throws ServletException {
        getServletContext().setAttribute("maxCommentDepth", 3);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(req);
        int postId = converter.getIntParameter("id").orElse(0); //viene ignorato se commentId != 0
        int commentId = converter.getIntParameter("comment").orElse(0);

        PostPage post;
        Map<Integer, List<CommentDTO>> comments;
        int initialIndex;
        if (commentId == 0) {
            post = postService.getPost(postId);
            comments = service.getPostComments(postId);
            initialIndex = 0; //si parte dai root comments
        } else{
            comments = service.getReplies(commentId);
            initialIndex = Collections.min(comments.keySet()); //il commento di partenza è quello all'indice più basso
            postId = comments.get(initialIndex).get(0).getPostId(); //prendi un commento qualsiasi, ricava il postId
            post = postService.getPost(postId);
        }

        req.setAttribute("post", post);
        req.setAttribute("comments", comments);
        req.setAttribute("initialIndex", initialIndex);
        req.getRequestDispatcher("/WEB-INF/views/section/post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}