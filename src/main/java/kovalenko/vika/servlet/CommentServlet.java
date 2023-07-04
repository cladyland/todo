package kovalenko.vika.servlet;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.service.CommentService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TASK_INFO_LINK;

@Slf4j
@WebServlet(name = "CommentServlet", value = COMMENT_LINK)
public class CommentServlet extends HttpServlet {
    private CommentService commentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        commentService = (CommentService) context.getAttribute(COMMENT_SERVICE);

        log.debug("'CommentServlet' initialized");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long taskId = Long.parseLong(req.getParameter(TASK_ID));

        CommentCommand command = buildCommentCommand(req, taskId);
        commentService.createComment(command);

        req.getSession().setAttribute(COMMENT_ADDED_TO_TASK, taskId);
        resp.sendRedirect(TASK_INFO_LINK);
    }

    private CommentCommand buildCommentCommand(HttpServletRequest req, Long taskId) {
        var username = (String) req.getSession().getAttribute(USERNAME);

        return CommentCommand.builder()
                .username(username)
                .taskId(taskId)
                .contents(req.getParameter(COMMENT))
                .build();
    }
}
