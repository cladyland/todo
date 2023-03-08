package kovalenko.vika.servlet;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static kovalenko.vika.enums.JSP.NEW_TAG;

@WebServlet(name = "NewTagServlet", value = "/todo/new-tag")
public class NewTagServlet extends HttpServlet {
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        tagService = (TagService) context.getAttribute("tagService");
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(NEW_TAG.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = (String) req.getSession().getAttribute("username");
        Long id = userService.getUserId(username);

        tagService.createTag(buildTagCommand(req, id));
        List<TagDTO> userTags = tagService.getUserTags(id);
        req.getSession().setAttribute("userTags", userTags);
        resp.sendRedirect("/todo");
    }

    private TagCommand buildTagCommand(HttpServletRequest req, Long userId){
        return TagCommand.builder()
                .userId(userId)
                .title(req.getParameter("title"))
                .color(req.getParameter("color"))
                .build();
    }
}
