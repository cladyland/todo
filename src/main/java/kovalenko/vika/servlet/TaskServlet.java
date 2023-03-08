package kovalenko.vika.servlet;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.TaskService;

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

import static kovalenko.vika.enums.JSP.TODO;
import static kovalenko.vika.utils.AttributeConstant.DELETE;
import static kovalenko.vika.utils.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TITLE;
import static kovalenko.vika.utils.AttributeConstant.USER_ATTR;

@WebServlet(name = "TaskServlet", value = "/todo")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        todoForward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter(DELETE) != null) {
            deleteTask(req);
        } else if (req.getParameter(SAVE_UPDATE) != null) {
            updateTask(req);
        }
        todoForward(req, resp);
    }

    private void updateTask(HttpServletRequest req) {
        Long id = Long.valueOf(req.getParameter(SAVE_UPDATE));
        var taskDTO = buildTaskDTO(req);
        taskDTO.setId(id);

        taskService.updateTask(taskDTO);

        var user = (UserDTO) req.getSession().getAttribute(USER_ATTR);
        List<TaskDTO> tasks = taskService.getAllUserTasks(user.getUsername());

        req.getSession().setAttribute(TASKS, tasks);
    }

    private void deleteTask(HttpServletRequest req) {
        String strId = req.getParameter(DELETE);
        Long id = Long.valueOf(strId);
        TaskDTO removedTask = taskService.deleteTask(id);
        HttpSession session = req.getSession();

        List<TaskDTO> tasks = getUserTasks(session);
        tasks
                .removeIf(task -> Objects.equals(task.getId(), removedTask.getId()));

        session.setAttribute(TASKS, tasks);
    }

    private TaskDTO buildTaskDTO(HttpServletRequest req) {
        return TaskDTO.builder()
                .title(req.getParameter(TITLE))
                .description(req.getParameter(DESCRIPTION))
                .build();
    }

    private List<TaskDTO> getUserTasks(HttpSession session) {
        return (List<TaskDTO>) session.getAttribute("tasks");
    }

    private void todoForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(TODO.getValue())
                .forward(req, resp);
    }
}
