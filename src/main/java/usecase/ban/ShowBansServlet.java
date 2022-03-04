package usecase.ban;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static usecase.auth.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/showbans")
@AuthorizationConstraints(ADMINS_ONLY)
class ShowBansServlet extends InterceptableServlet {
    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int userId = converter.getIntParameter("userId").orElse(0);
        List<BanDTO> bans = service.retrieveUserBan(userId);
        request.setAttribute("bans", bans);
        request.setAttribute("userId", userId);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-bans.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
