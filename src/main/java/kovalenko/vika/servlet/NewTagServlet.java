package kovalenko.vika.servlet;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static kovalenko.vika.enums.JSP.NEW_TAG;
import static kovalenko.vika.utils.AttributeConstant.COLOR;
import static kovalenko.vika.utils.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TITLE;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USER_TAGS;
import static kovalenko.vika.utils.LinkConstant.NEW_TAG_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebServlet(name = "NewTagServlet", value = NEW_TAG_LINK)
public class NewTagServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(NewTagServlet.class);
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        tagService = (TagService) context.getAttribute(TAG_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);

        LOG.debug("'NewTagServlet' initialized");
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
        var username = (String) req.getSession().getAttribute(USERNAME);
        Long id = userService.getUserId(username);

        tagService.createTag(buildTagCommand(req, id));

        List<TagDTO> userTags = tagService.getUserTags(id);
        req.getSession().setAttribute(USER_TAGS, userTags);
        resp.sendRedirect(TODO_LINK);
    }

    private TagCommand buildTagCommand(HttpServletRequest req, Long userId) {
        return TagCommand.builder()
                .userId(userId)
                .title(req.getParameter(TITLE))
                .color(req.getParameter(COLOR))
                .build();
    }
}