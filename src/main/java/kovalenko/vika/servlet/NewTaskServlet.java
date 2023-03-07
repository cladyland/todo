package kovalenko.vika.servlet;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;
import static kovalenko.vika.enums.JSP.NEW_TASK;

@WebServlet(name = "NewTaskServlet", value = "/todo/new-task")
public class NewTaskServlet extends HttpServlet {
    private TaskService taskService;
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute("taskService");
        tagService = (TagService) context.getAttribute("tagService");
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isNull(req.getSession().getAttribute("tags"))){
            req.getSession().setAttribute("tags", tagService.getDefaultTags());
        }

        req
                .getServletContext()
                .getRequestDispatcher(NEW_TASK.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        var task = buildTaskDTO(req);
        var user = (UserDTO) session.getAttribute("user");

        TaskDTO addedTask = taskService.createTask(task, userService.getUserId(user.getUsername()));

        List<TaskDTO> tasks = getUserTasks(session);
        tasks.add(addedTask);
        session.setAttribute("tasks", tasks);
        resp.sendRedirect("/todo");
    }

    private TaskDTO buildTaskDTO(HttpServletRequest req) {
        return TaskDTO.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .build();
    }

    private List<TaskDTO> getUserTasks(HttpSession session) {
        return (List<TaskDTO>) session.getAttribute("tasks");
    }
}
