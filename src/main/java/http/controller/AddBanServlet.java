package http.controller;

import http.util.ParameterConverter;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;

@WebServlet("/admin/addban")
public class AddBanServlet extends HttpServlet {
    @Inject private BanService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        Instant endDate = converter.getDateParameter("endDate")
                .map(date -> date.atStartOfDay().toInstant(ZoneOffset.UTC)) //trasforma solo se l'optional non è vuoto
                .orElse(null);
        int userId = converter.getIntParameter("userId").orElse(0);
        service.addBan(endDate,userId);
    }
}
