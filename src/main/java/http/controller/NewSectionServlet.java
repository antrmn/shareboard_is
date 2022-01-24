package http.controller;

import http.util.ParameterConverter;
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

@WebServlet("/admin/newsection")
@MultipartConfig
public class NewSectionServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Part picture = request.getPart("picture");
        Part banner = request.getPart("banner");

        //gestire errore immagine

        SectionPage sectionPage = new SectionPage(0,name,description,picture.getName(),banner.getName(),0);
        BufferedInputStream buffPicture = new BufferedInputStream(picture.getInputStream());
        BufferedInputStream buffBanner = new BufferedInputStream(banner.getInputStream());
        service.newSection(sectionPage,buffPicture,buffBanner);
        response.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
