package http.controller;

import http.util.ParameterConverter;
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
    @Inject private ParameterConverter converter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Instant endDate = converter.getDateParameter("endDate")
                .map(date -> date.atStartOfDay().toInstant(ZoneOffset.UTC)) //trasforma solo se l'optional non è vuoto
                .orElse(null);
        int userId = converter.getIntParameter("userId").orElse(0);
        service.addBan(endDate,userId);
    }
}
