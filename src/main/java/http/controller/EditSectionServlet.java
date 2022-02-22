package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.util.interceptor.InterceptableServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static http.controller.interceptor.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/editsection")
@MultipartConfig
@AuthorizationConstraints(ADMINS_ONLY)
public class EditSectionServlet extends InterceptableServlet {
    //Funzionalità disabilitata

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN,"Funzionalità disabilitata temporaneamente");
    }



    /*@Inject private SectionService service;

    private static final String EDIT_SECTION_PAGE = "/WEB-INF/views/crm/edit-section.jsp";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int sectionId = converter.getIntParameter("sectionId").orElse(0);
        SectionPage section = service.showSection(sectionId);
        request.setAttribute("section", section);
        request.getRequestDispatcher(EDIT_SECTION_PAGE).forward(request, response);
    }

    @Override
    @ForwardOnError(EDIT_SECTION_PAGE)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int sectionId = converter.getIntParameter("sectionId").orElse(0);
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        Part banner = request.getPart("banner");

        BufferedInputStream buffPicture = null;
        BufferedInputStream buffBanner = null;
        if(picture != null && picture.getSize() < MAX_FILE_SIZE) {
            if (picture.getSize()>0)
                buffPicture = new BufferedInputStream(picture.getInputStream());
        }else{
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        }

        if(banner != null && banner.getSize() < MAX_FILE_SIZE) {
            if (banner.getSize()>0)
                buffBanner = new BufferedInputStream(banner.getInputStream());
        }else{
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        }

        SectionPage sectionPage = new SectionPage(sectionId,null,description,picture.getName(),banner.getName(),0,false); //false?
        service.editSection(sectionPage,sectionId,buffPicture,buffBanner);
        response.sendRedirect(request.getContextPath()+"/admin/showsections");
    }
    */
}
