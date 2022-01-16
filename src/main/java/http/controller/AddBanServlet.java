package http.controller;

import service.BanService;

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("endDate");
        int userId = request.getParameter("userId").matches("\\d*") ? Integer.parseInt(request.getParameter("userId")) : 0;
        Instant endDate = date != null && !date.isEmpty() ? LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        new BanService().addBan(endDate,userId);
    }
}
