package usecase.section;

import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

/**
 * Servlet che mostra agli amministratori le sezioni esistenti.
 */
@WebServlet("/admin/showsections")
@AuthorizationConstraints(ADMINS_ONLY)
class ShowSectionServlet extends InterceptableServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/crm/show-sections.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
