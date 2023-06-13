package kovalenko.vika.servlet;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.exception.TaskException;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
import kovalenko.vika.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static kovalenko.vika.enums.JSP.NEW_TAG;
import static kovalenko.vika.utils.constants.AttributeConstant.COLOR;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_TAGS;
import static kovalenko.vika.utils.constants.LinkConstant.TAG_LINK;

@Slf4j
@WebServlet(name = "TagServlet", value = TAG_LINK)
public class TagServlet extends HttpServlet {
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        tagService = (TagService) context.getAttribute(TAG_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);

        log.debug("'NewTagServlet' initialized");
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

        try {
            tagService.createTag(buildTagCommand(req, id));
        } catch (TaskException ex) {
            ServletUtil.forwardWithErrorMessage(req, resp, ex.getMessage(), NEW_TAG.getValue());
            return;
        }

        List<TagDTO> userTags = tagService.getUserTags(id);
        req.getSession().setAttribute(USER_TAGS, userTags);
        req
                .getServletContext()
                .getRequestDispatcher(NEW_TAG.getValue())
                .forward(req, resp);
    }

    private TagCommand buildTagCommand(HttpServletRequest req, Long userId) {
        return TagCommand.builder()
                .userId(userId)
                .title(req.getParameter(TITLE))
                .color(req.getParameter(COLOR))
                .build();
    }
}
