package http.controller;

import persistence.model.Ban;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/showbans")
public class ShowBansServlet extends HttpServlet {

    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _userId = request.getParameter("userId");
        int userId = 0;
        if(_userId != null && _userId.matches("\\d*")){
            userId = Integer.parseInt(_userId);
        }
        List<Ban> bans = service.retrieveUserBan(userId);
        request.setAttribute("bans", bans);
        request.setAttribute("userId", userId);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-bans.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
