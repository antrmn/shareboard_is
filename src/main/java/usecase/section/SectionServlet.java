package usecase.section;

import common.http.interceptor.InterceptableServlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet che permette la visualizzazione di una sezione.
 */
@WebServlet("/s")
class SectionServlet extends InterceptableServlet {

    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sectionName = request.getParameter("section");
        SectionPage section = service.getSection(sectionName);
        request.setAttribute("section", section);
        request.getRequestDispatcher("/WEB-INF/views/section/section.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
