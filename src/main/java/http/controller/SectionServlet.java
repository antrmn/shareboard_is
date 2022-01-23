package http.controller;

import persistence.model.Section;
import service.SectionService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/s")
public class SectionServlet extends HttpServlet {

    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sectionName = request.getParameter("section");
        Section section = service.getSection(sectionName);
        request.setAttribute("section", section);
        request.getRequestDispatcher("/WEB-INF/views/section/section.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
