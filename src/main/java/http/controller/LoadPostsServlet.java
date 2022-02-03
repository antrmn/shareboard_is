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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.function.Function;

import static service.dto.PostSearchForm.SortCriteria.*;

@WebServlet("/loadposts")
public class LoadPostsServlet extends HttpServlet {
    @Inject ParameterConverter converter;

    private static final Function<LocalDate, Instant> LOCALDATE_TO_INSTANT =
            x -> x.atStartOfDay().toInstant(ZoneOffset.UTC);

    private static final Map<String, PostSearchForm.SortCriteria> SORT_CRITERIA = Map.of(
            "oldest", OLDEST,
            "newest", NEWEST,
            "mostvoted", MOSTVOTED);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = req.getParameter("content");
        String section = req.getParameter("section");
        String author = req.getParameter("author");
        Instant postedAfter = converter.getDateParameter("postedAfter")
                .map(LOCALDATE_TO_INSTANT).orElse(null);
        Instant postedBefore = converter.getDateParameter("postedBefore")
                .map(LOCALDATE_TO_INSTANT).orElse(null);
        PostSearchForm.SortCriteria orderBy = SORT_CRITERIA.get(req.getParameter("orderby"));
        int page = converter.getIntParameter("page").orElse(1);
        boolean onlyFollow = req.getParameter("onlyfollow") != null;
        boolean includeBody = req.getParameter("includeBody") != null;

        PostSearchForm postSearchForm = PostSearchForm.builder()
                .content(content)
                .onlyFollow(onlyFollow)
                .includeBody(includeBody)
                .sectionName(section)
                .authorName(author)
                .orderBy(orderBy)
                .postedAfter(postedAfter)
                .postedBefore(postedBefore)
                .page(page)
                .build();

        //TODO: prendi sezioni, ottieni top all time e top weekly
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
