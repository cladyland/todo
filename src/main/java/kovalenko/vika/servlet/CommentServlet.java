package kovalenko.vika.servlet;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.service.CommentService;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static kovalenko.vika.utils.AttributeConstant.COMMENT_ADD_TO_TASK;
import static kovalenko.vika.utils.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_INFO_LINK;

@WebServlet(name = "CommentServlet", value = COMMENT_LINK)
public class CommentServlet extends HttpServlet {
    private CommentService commentService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        commentService = (CommentService) context.getAttribute(COMMENT_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        var username = (String) session.getAttribute(USERNAME);
        Long taskId = Long.parseLong(req.getParameter(TASK));
        CommentCommand command = CommentCommand.builder()
                .userId(getUserId(username))
                .taskId(taskId)
                .contents(req.getParameter("comment"))
                .build();
        commentService.createComment(command);
        session.setAttribute(COMMENT_ADD_TO_TASK, taskId);
        resp.sendRedirect(TODO_INFO_LINK);
    }

    private Long getUserId(String username){
        return userService.getUserId(username);
    }
}
