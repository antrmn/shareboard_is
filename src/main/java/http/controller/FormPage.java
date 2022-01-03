package http.controller;

import service.exception.BadRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FormPage extends HttpServlet {
    protected abstract String getFormPage();

    protected String getFormErrorPage(){
        return getFormPage();
    }

    protected abstract String submitForm();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(getFormPage());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        req.setAttribute("errors" , errors);
        try{
            String redirect = submitForm();
            resp.sendRedirect(redirect);
        } catch (BadRequestException e){
            errors.addAll(e.getMessages());
            req.getRequestDispatcher(getFormErrorPage()).forward(req, resp);
        }
    }
}
