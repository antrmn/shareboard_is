package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.controller.interceptor.ForwardOnError;
import http.util.interceptor.InterceptableServlet;
import service.SectionService;
import service.dto.SectionPage;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.IOException;

import static http.controller.interceptor.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/newsection")
@MultipartConfig
@AuthorizationConstraints(ADMINS_ONLY)
public class NewSectionServlet extends InterceptableServlet {
    @Inject private SectionService service;

    private static final String NEW_SECTION_PAGE = "/WEB-INF/views/crm/create-section.jsp";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
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

        //gestire errore immagine
        BufferedInputStream buffPicture = null;
        BufferedInputStream buffBanner = null;
        if(picture != null && picture.getSize() < MAX_FILE_SIZE) {
            if (picture.getSize()>0) {
                System.out.println("HERE");
                buffPicture = new BufferedInputStream(picture.getInputStream());
            }
        }else{
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        }

        if(banner != null && banner.getSize() < MAX_FILE_SIZE) {
            if (banner.getSize()>0)
                buffBanner = new BufferedInputStream(picture.getInputStream());
        }else{
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        }


        SectionPage sectionPage = new SectionPage(0,name,description,picture.getName(),banner.getName(),0,false);
        service.newSection(sectionPage,buffPicture,buffBanner);
        response.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
