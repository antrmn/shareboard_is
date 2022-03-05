package usecase.post;

import common.http.ParameterConverter;
import common.http.error.ForwardOnError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;
import usecase.section.SectionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet("/newpost")
@MultipartConfig
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class NewPostServlet extends InterceptableServlet {
    @Inject private PostService service;
    @Inject private SectionService sectionService;

    private static final String NEW_POST_PAGE = "/WEB-INF/views/section/create-post.jsp";

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
            BufferedInputStream stream = picture.getSize() > 0 ? new BufferedInputStream(picture.getInputStream()) : null;
            newPostId = service.newPost(title,stream, sectionName);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + newPostId);
    }
}
