package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import service.SectionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static http.controller.interceptor.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet(name = "DeleteSectionServlet", value = "/deleteSection")
@AuthorizationConstraints(ADMINS_ONLY)
public class DeleteSectionServlet extends InterceptableServlet {
    @Inject private SectionService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int sectionId = converter.getIntParameter("sectionId").orElse(0);
        service.delete(sectionId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showsections");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
