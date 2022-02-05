package http.controller;

import http.controller.interceptor.ForwardOnError;
import http.util.ParameterConverter;
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

@WebServlet("/admin/newsection")
@MultipartConfig
public class NewSectionServlet extends InterceptableServlet {
    @Inject private ParameterConverter converter;
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

        //gestire errore immagine

        SectionPage sectionPage = new SectionPage(0,name,description,picture.getName(),banner.getName(),0,true);
        BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
        BufferedInputStream buffBanner = new BufferedInputStream(banner.getInputStream());
        service.newSection(sectionPage,buffPicture,buffBanner);
        response.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
