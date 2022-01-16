package http.controller;

import persistence.model.Ban;
import service.BanService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/showbans")
public class ShowBansServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = request.getParameter("userId").matches("\\d*") ? Integer.parseInt(request.getParameter("userId")) : 0;
        List<Ban> bans = new BanService().retrieveUserBan(userId);
        request.setAttribute("bans", bans);
        request.setAttribute("userId", userId);
        request.getRequestDispatcher("/WEB-INF/views/crm/show-bans.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
