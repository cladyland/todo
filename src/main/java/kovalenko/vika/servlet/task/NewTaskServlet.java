package kovalenko.vika.servlet.task;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
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
import java.util.Set;

import static kovalenko.vika.enums.JSP.NEW_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITIES;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUS;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUSES;
import static kovalenko.vika.utils.constants.AttributeConstant.TASKS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.AttributeConstant.TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ID;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebServlet(name = "NewTaskServlet", value = NEW_TASK_LINK)
public class NewTaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);

        log.debug("'NewTaskServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setPrioritiesAndStatuses(req);

        req
                .getServletContext()
                .getRequestDispatcher(NEW_TASK.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        var taskTagsIds = (Set<Long>) req.getAttribute(TASK_TAGS);

        TaskCommand command = buildTaskCommand(req);
        TaskDTO addedTask;
        try {
            addedTask = taskService.createTask(command, taskTagsIds);
            log.debug("Added new task with id '{}'", addedTask.getId());
        } catch (TaskException ex) {
            setPrioritiesAndStatuses(req);
            ServletUtil.forwardWithErrorMessage(req, resp, ex.getMessage(), NEW_TASK.getValue());

            log.warn("Failed to add a new task: {}", ex.getMessage());
            return;
        }

        List<TaskDTO> tasks = getUserTasks(session);
        tasks.add(addedTask);
        session.setAttribute(TASKS, tasks);
        resp.sendRedirect(TODO_LINK);
    }

    private void setPrioritiesAndStatuses(HttpServletRequest request) {
        request.setAttribute(PRIORITIES, taskService.getPriorities());
        request.setAttribute(STATUSES, taskService.getStatuses());
    }

    private TaskCommand buildTaskCommand(HttpServletRequest req) {
        Long id = (Long) req.getAttribute(USER_ID);
        var taskPriority = TaskPriority.getPriorityByName(req.getParameter(PRIORITY));
        var taskStatus = TaskStatus.getStatusByName(req.getParameter(STATUS));

        return TaskCommand.builder()
                .userId(id)
                .title(req.getParameter(TITLE))
                .description(req.getParameter(DESCRIPTION))
                .priority(taskPriority)
                .status(taskStatus)
                .build();
    }

    private List<TaskDTO> getUserTasks(HttpSession session) {
        return (List<TaskDTO>) session.getAttribute(TASKS);
    }
}
