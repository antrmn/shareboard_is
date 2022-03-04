package usecase.section;

import common.http.error.ForwardOnError;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/newsection")
@MultipartConfig
@AuthorizationConstraints(ADMINS_ONLY)
public class NewSectionServlet extends InterceptableServlet {
    @Inject private SectionService service;

    private static final String NEW_SECTION_PAGE = "/WEB-INF/views/crm/create-section.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(request, response);
    }

    @Override
    @ForwardOnError(NEW_SECTION_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        Part banner = request.getPart("banner");

        BufferedInputStream streamPicture = picture.getSize() > 0 ? new BufferedInputStream(picture.getInputStream()) : null;
        BufferedInputStream streamBanner = banner.getSize() > 0 ? new BufferedInputStream(picture.getInputStream()) : null;

        service.newSection(name, description ,streamPicture,streamBanner);
        response.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
