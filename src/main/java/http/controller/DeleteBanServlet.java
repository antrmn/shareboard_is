package http.controller;

import service.BanService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/deleteban")
public class DeleteBanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _bandId = request.getParameter("banId");
        int banId = 0;
        if(_bandId != null && _bandId.matches("\\d*")){
            banId = Integer.parseInt(_bandId);
        }
        new BanService().removeBan(banId);
        response.sendRedirect(getServletContext().getContextPath() + "/admin/showbans?userId=" + request.getParameter("userId"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
