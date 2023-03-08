package kovalenko.vika.servlet;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (isNull(req.getSession().getAttribute("userTags"))){
            var username = (String) req.getSession().getAttribute("username");
            Long id = userService.getUserId(username);
            req.getSession().setAttribute("userTags", tagService.getUserTags(id));
        }
        req.setAttribute("priorities", taskService.getPriorities());
        req.setAttribute("statuses", taskService.getStatuses());

        req
                .getServletContext()
                .getRequestDispatcher(NEW_TASK.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String[] taskTagsIds = req.getParameterValues("taskTags");
        Set<TagDTO> tags = tagService.getTagsByIds(convertIds(taskTagsIds));

        TaskCommand command = buildTaskCommand(req, tags);
        TaskDTO addedTask = taskService.createTask(command, convertIds(taskTagsIds));

        List<TaskDTO> tasks = getUserTasks(session);
        tasks.add(addedTask);
        session.setAttribute("tasks", tasks);
        resp.sendRedirect("/todo");
    }

    private TaskCommand buildTaskCommand(HttpServletRequest req, Set<TagDTO> tags){
        var username = (String) req.getSession().getAttribute("username");
        Long id = userService.getUserId(username);
        var taskPriority = TaskPriority.getPriorityByName(req.getParameter("priority"));
        var taskStatus = TaskStatus.getStatusByName(req.getParameter("status"));

        return TaskCommand.builder()
                .userId(id)
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .priority(taskPriority)
                .status(taskStatus)
                .tags(tags)
                .build();
    }

    private Set<Long> convertIds(String[] ids){
        return Arrays.stream(ids)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    private List<TaskDTO> getUserTasks(HttpSession session) {
        return (List<TaskDTO>) session.getAttribute("tasks");
    }
}
