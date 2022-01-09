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
import java.util.Objects;

public abstract class FormPageServlet extends HttpServlet {
    protected abstract void viewPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected abstract void viewErrorpage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected abstract void submitForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        viewPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            submitForm(req, resp);
        } catch (BadRequestException e) {
            ArrayList<String> errors = new ArrayList<>(e.getMessages());
            req.setAttribute("errors", errors);
            viewErrorpage(req, resp);
        }
    }
}
