package http.controller;

import service.SectionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteSectionServlet", value = "/DeleteSectionServlet")
public class DeleteSectionServlet extends HttpServlet {

    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _sectionId = request.getParameter("sectionId");
        int sectionId = 0;
        if(_sectionId != null && _sectionId.matches("\\d*")){
            sectionId = Integer.parseInt(_sectionId);
        }

        service.delete(sectionId);

        response.sendRedirect(getServletContext().getContextPath() + "/admin/showsections");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
