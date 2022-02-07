package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.controller.interceptor.JSONError;
import http.util.ParameterConverter;
import http.util.interceptor.InterceptableServlet;
import service.BanService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;

import static http.controller.interceptor.AuthorizationConstraints.Types.ADMINS_ONLY;

@WebServlet("/admin/addban")
@AuthorizationConstraints(ADMINS_ONLY)
public class AddBanServlet extends InterceptableServlet {
    @Inject private BanService service;

    @Override
    @JSONError
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    @JSONError
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        Instant endDate = converter.getDateParameter("endDate")
                .map(date -> date.atStartOfDay().toInstant(ZoneOffset.UTC)) //trasforma solo se l'optional non è vuoto
                .orElse(null);
        int userId = converter.getIntParameter("userId").orElse(0);
        service.addBan(endDate,userId);
    }
}
