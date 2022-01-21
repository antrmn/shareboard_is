package http.controller;

import service.SectionService;
import service.dto.SectionPage;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedInputStream;
import java.io.IOException;

@WebServlet("/admin/newsection")
@MultipartConfig
public class NewSectionServlet extends HttpServlet {
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
        new SectionService().NewSection(sectionPage,buffPicture,buffBanner);
        response.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
