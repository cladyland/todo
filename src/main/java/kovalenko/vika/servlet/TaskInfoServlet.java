package kovalenko.vika.servlet;

import kovalenko.vika.service.CommentService;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.utils.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;

@WebServlet(name = "TaskInfoServlet", value = "/todo/info")
public class TaskInfoServlet extends HttpServlet {
    private TaskService taskService;
    private CommentService commentService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        commentService = (CommentService) context.getAttribute(COMMENT_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
