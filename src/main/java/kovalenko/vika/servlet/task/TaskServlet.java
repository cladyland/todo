package kovalenko.vika.servlet.task;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.TaskException;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;

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
import static kovalenko.vika.enums.JSP.TASK_UPDATE;
import static kovalenko.vika.enums.JSP.TODO;
import static kovalenko.vika.utils.constants.AttributeConstant.DELETE;
import static kovalenko.vika.utils.constants.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.constants.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASKS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.AttributeConstant.TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebServlet(name = "TaskServlet", value = TODO_LINK)
public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);

        log.debug("'TaskServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        todoForward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deleteId = req.getParameter(DELETE);
        String saveUpdateId = req.getParameter(SAVE_UPDATE);

        if (nonNull(deleteId)) {
            deleteTask(req, Long.valueOf(deleteId));
        } else if (nonNull(saveUpdateId)) {
            Long taskId = Long.valueOf(saveUpdateId);

            try {
                updateTask(req, taskId);
            } catch (TaskException ex) {
                ServletUtil.setRequestAttributesForUpdatingTask(req, taskService.getTaskById(taskId));
                ServletUtil.forwardWithErrorMessageAndStatus400(req, resp, ex.getMessage(), TASK_UPDATE.getValue());

                log.warn("Failed to update task '{}': {}", taskId, ex.getMessage());
                return;
            }
        }

        todoForward(req, resp);
    }

    private void deleteTask(HttpServletRequest req, Long taskId) {
        taskService.deleteTask(taskId);

        HttpSession session = req.getSession();
        var tasks = (List<TaskDTO>) session.getAttribute(TASKS);

        tasks
                .removeIf(task -> Objects.equals(task.getId(), taskId));

        session.setAttribute(TASKS, tasks);
    }

    private void updateTask(HttpServletRequest req, Long taskId) {
        var taskDTO = buildTaskDTO(req, taskId);
        var taskTagsIds = (Set<Long>) req.getAttribute(TASK_TAGS);

        taskService.updateTask(taskDTO, taskTagsIds);

        var user = (UserDTO) req.getSession().getAttribute(USER_ATTR);
        List<TaskDTO> tasks = taskService.getAllUserTasks(user.getUsername());

        req.getSession().setAttribute(TASKS, tasks);
    }

    private TaskDTO buildTaskDTO(HttpServletRequest req, Long taskId) {
        return TaskDTO.builder()
                .id(taskId)
                .title(ServletUtil.checkIfParameterContentChanged(req, TITLE))
                .description(ServletUtil.checkIfParameterContentChanged(req, DESCRIPTION))
                .priority(req.getParameter(PRIORITY))
                .status(req.getParameter(STATUS))
                .build();
    }

    private void todoForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(TODO.getValue())
                .forward(req, resp);
    }
}
