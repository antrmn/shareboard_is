package http.controller;

import http.util.ParameterConverter;
import persistence.model.Section;
import service.SectionService;
import service.dto.SectionPage;

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

@WebServlet("/admin/editsection")
@MultipartConfig
public class EditSectionServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sectionId = converter.getIntParameter("sectionId").orElse(0);
        Section section = service.getSection(sectionId);
        request.setAttribute("section", section);
        request.getRequestDispatcher("/WEB-INF/views/crm/edit-section.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sectionId = converter.getIntParameter("sectionId").orElse(0);
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        Part banner = request.getPart("banner");

        SectionPage sectionPage = new SectionPage(sectionId,null,description,picture.getName(),banner.getName(),0);
        BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
        BufferedInputStream buffBanner = new BufferedInputStream(banner.getInputStream());
        service.editSection(sectionPage,sectionId,buffPicture,buffBanner);
        response.sendRedirect(request.getContextPath()+"/admin/showsections");
    }
}
