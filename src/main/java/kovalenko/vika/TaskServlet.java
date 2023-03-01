package kovalenko.vika;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static kovalenko.vika.JSP.TODO;

@WebServlet(name = "TaskServlet", value = "/todo")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute("taskService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(TODO.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskDTO taskDTO = TaskDTO.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .build();
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        taskService.createTask(taskDTO, userDTO);

        List<Task> tasks = taskService.getAllUserTasks(userDTO);
        req.setAttribute("tasks", tasks);
        req.getServletContext().getRequestDispatcher(TODO.getValue()).forward(req, resp);
    }
}
