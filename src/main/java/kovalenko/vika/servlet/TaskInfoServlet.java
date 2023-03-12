package kovalenko.vika.servlet;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;
import static kovalenko.vika.enums.JSP.TASK_INFO;
import static kovalenko.vika.utils.AttributeConstant.TASK;
import static kovalenko.vika.utils.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@WebServlet(name = "TaskInfoServlet", value = TASK_INFO_LINK)
public class TaskInfoServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TaskInfoServlet.class);
    private TaskService taskService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);

        LOG.debug("'TaskInfoServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setTaskAttribute(req, resp);

        req
                .getServletContext()
                .getRequestDispatcher(TASK_INFO.getValue())
                .forward(req, resp);
    }

    private void setTaskAttribute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = (Long) req.getAttribute(TASK_ID);
        TaskDTO task = taskService.getTaskById(taskId);
        if (isNull(task)){
            resp.sendRedirect(NOT_FOUND_LINK);
            LOG.warn("Someone tried to access a task with a nonexistent ID");
            return;
        }
        req.setAttribute(TASK, task);
    }
}
