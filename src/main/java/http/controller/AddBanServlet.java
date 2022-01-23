package http.controller;

import service.BanService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@WebServlet("/admin/addban")
@MultipartConfig
public class AddBanServlet extends HttpServlet {

    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("endDate");
        String _userId = request.getParameter("userId");
        int userId = 0;
        if(_userId != null && _userId.matches("\\d*")){
            userId = Integer.parseInt(_userId);
        }
        Instant endDate = date != null && !date.isEmpty() ? LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        service.addBan(endDate,userId);
    }
}
