package kovalenko.vika.servlet.task;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.nonNull;
import static kovalenko.vika.enums.JSP.TODO;
import static kovalenko.vika.utils.AttributeConstant.DELETE;
import static kovalenko.vika.utils.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.AttributeConstant.STATUS;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.AttributeConstant.TITLE;
import static kovalenko.vika.utils.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebServlet(name = "TaskServlet", value = TODO_LINK)
public class TaskServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServlet.class);
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);

        LOG.debug("'TaskServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        todoForward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (nonNull(req.getParameter(DELETE))) {
            deleteTask(req);
        } else if (nonNull(req.getParameter(SAVE_UPDATE))) {
            updateTask(req);
        }

        todoForward(req, resp);
    }

    private void updateTask(HttpServletRequest req) {
        var taskDTO = buildTaskDTO(req);
        var taskTagsIds = (Set<Long>) req.getAttribute(TASK_TAGS);

        taskService.updateTask(taskDTO, taskTagsIds);

        var user = (UserDTO) req.getSession().getAttribute(USER_ATTR);
        List<TaskDTO> tasks = taskService.getAllUserTasks(user.getUsername());

        req.getSession().setAttribute(TASKS, tasks);
    }

    private void deleteTask(HttpServletRequest req) {
        Long taskId = Long.valueOf(req.getParameter(DELETE));
        TaskDTO removedTask = taskService.deleteTask(taskId);
        HttpSession session = req.getSession();

        List<TaskDTO> tasks = getUserTasks(session);
        Long removedTaskId = removedTask.getId();

        tasks
                .removeIf(task -> Objects.equals(task.getId(), removedTaskId));

        session.setAttribute(TASKS, tasks);
    }

    private TaskDTO buildTaskDTO(HttpServletRequest req) {
        var taskId = Long.valueOf(req.getParameter(SAVE_UPDATE));

        return TaskDTO.builder()
                .id(taskId)
                .title(req.getParameter(TITLE))
                .description(req.getParameter(DESCRIPTION))
                .priority(req.getParameter(PRIORITY))
                .status(req.getParameter(STATUS))
                .build();
    }

    private List<TaskDTO> getUserTasks(HttpSession session) {
        return (List<TaskDTO>) session.getAttribute(TASKS);
    }

    private void todoForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(TODO.getValue())
                .forward(req, resp);
    }
}
