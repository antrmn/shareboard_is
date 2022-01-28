package http.controller;

import http.util.ParameterConverter;
import service.dto.PostSearchForm;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;

@WebServlet("/loadposts")
public class LoadPostsServlet extends HttpServlet {
    @Inject ParameterConverter converter;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = req.getParameter("content");
        String onlyFollow = req.getParameter("onlyfollow");
        String section = req.getParameter("section");
        String author = req.getParameter("author");
        String orderBy = req.getParameter("orderby");
        Instant postedAfter = converter.getDateParameter("postedAfter")
                .map(x -> x.atStartOfDay().toInstant(ZoneOffset.UTC)).orElse(null);
        Instant postedBefore = converter.getDateParameter("postedBefore")
                .map(x -> x.atStartOfDay().toInstant(ZoneOffset.UTC)).orElse(null);
        int page = converter.getIntParameter("page").orElse(1);

        //TODO: INCOMPLETO
        PostSearchForm postSearchForm = PostSearchForm.builder()
                .content(content)
                .onlyFollow(onlyFollow != null)
                .sectionName(section)
                .authorName(author)
                //.orderBy()
                .postedAfter(postedAfter)
                .postedBefore(postedBefore)
                .page(page)
                //.
                .build();

        //TODO: prendi sezioni, ottieni top all time e top weekly
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
