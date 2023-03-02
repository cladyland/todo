package kovalenko.vika;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kovalenko.vika.JSP.TASK_UPDATE;
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
        if(req.getSession().getAttribute("tasks") == null){
            UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
            List<TaskDTO> tasks = taskService.getAllUserTasks(userDTO);
            req.getSession().setAttribute("tasks", tasks);
            req.setAttribute("tasks", tasks);
        }

        if (req.getParameter("update") != null){
            String taskId = req.getParameter("update");
            TaskDTO taskDTO = taskService.getTaskById(Long.valueOf(taskId));
            List<TaskDTO> taskDTOS = new ArrayList<>();
            taskDTOS.add(taskDTO);

            req.setAttribute("task", taskDTOS);
            req
                    .getServletContext()
                    .getRequestDispatcher(TASK_UPDATE.getValue())
                    .forward(req, resp);
        }

        req
                .getServletContext()
                .getRequestDispatcher(TODO.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("delete") != null) {
            doDelete(req, resp);
        }
        if (req.getParameter("saveUpdate") != null){
            doPut(req, resp);
        }

        TaskDTO taskDTO = TaskDTO.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .build();
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        taskService.createTask(taskDTO, userDTO);

        List<TaskDTO> tasks = taskService.getAllUserTasks(userDTO);
        req.setAttribute("tasks", tasks);
        req.getSession().setAttribute("tasks", tasks);
        req.getServletContext().getRequestDispatcher(TODO.getValue()).forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskDTO taskDTO = TaskDTO.builder()
                .id(Long.valueOf(req.getParameter("saveUpdate")))
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .build();

        taskService.updateTask(taskDTO);
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        List<TaskDTO> tasks = taskService.getAllUserTasks(userDTO);
        req.getSession().setAttribute("tasks", tasks);
        req.setAttribute("tasks", tasks);
        req.getServletContext().getRequestDispatcher(TODO.getValue()).forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("delete");
        Long id = Long.valueOf(strId);
        taskService.deleteTask(id);
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        List<TaskDTO> tasks = taskService.getAllUserTasks(userDTO);
        req.getSession().setAttribute("tasks", tasks);
        req.setAttribute("tasks", tasks);
        req.getServletContext().getRequestDispatcher(TODO.getValue()).forward(req, resp);
    }
}
