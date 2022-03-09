package usecase.section;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

/**
 * Servlet che permette ad un amministratore di eliminare una sezione.
 */
@WebServlet("/admin/deleteSection")
@AuthorizationConstraints(ADMINS_ONLY)
class DeleteSectionServlet extends InterceptableServlet {
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
