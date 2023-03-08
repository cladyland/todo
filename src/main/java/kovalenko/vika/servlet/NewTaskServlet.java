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
import static kovalenko.vika.utils.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.AttributeConstant.PRIORITIES;
import static kovalenko.vika.utils.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.AttributeConstant.STATUS;
import static kovalenko.vika.utils.AttributeConstant.STATUSES;
import static kovalenko.vika.utils.AttributeConstant.TAGS;
import static kovalenko.vika.utils.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.AttributeConstant.TITLE;
import static kovalenko.vika.utils.AttributeConstant.TODO_LINK;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USER_TAGS;

@WebServlet(name = "NewTaskServlet", value = "/todo/new-task")
public class NewTaskServlet extends HttpServlet {
    private TaskService taskService;
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        tagService = (TagService) context.getAttribute(TAG_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isNull(req.getSession().getAttribute(TAGS))){
            req.getSession().setAttribute(TAGS, tagService.getDefaultTags());
        }
        if (isNull(req.getSession().getAttribute(USER_TAGS))){
            var username = (String) req.getSession().getAttribute(USERNAME);
            Long id = userService.getUserId(username);
            req.getSession().setAttribute(USER_TAGS, tagService.getUserTags(id));
        }
        req.setAttribute(PRIORITIES, taskService.getPriorities());
        req.setAttribute(STATUSES, taskService.getStatuses());

        req
                .getServletContext()
                .getRequestDispatcher(NEW_TASK.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String[] taskTagsIds = req.getParameterValues(TASK_TAGS);
        Set<TagDTO> tags = tagService.getTagsByIds(convertIds(taskTagsIds));

        TaskCommand command = buildTaskCommand(req, tags);
        TaskDTO addedTask = taskService.createTask(command, convertIds(taskTagsIds));

        List<TaskDTO> tasks = getUserTasks(session);
        tasks.add(addedTask);
        session.setAttribute(TASKS, tasks);
        resp.sendRedirect(TODO_LINK);
    }

    private TaskCommand buildTaskCommand(HttpServletRequest req, Set<TagDTO> tags){
        var username = (String) req.getSession().getAttribute(USERNAME);
        Long id = userService.getUserId(username);
        var taskPriority = TaskPriority.getPriorityByName(req.getParameter(PRIORITY));
        var taskStatus = TaskStatus.getStatusByName(req.getParameter(STATUS));

        return TaskCommand.builder()
                .userId(id)
                .title(req.getParameter(TITLE))
                .description(req.getParameter(DESCRIPTION))
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
        return (List<TaskDTO>) session.getAttribute(TASKS);
    }
}