package kovalenko.vika.filter.task;

import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kovalenko.vika.enums.JSP.TASK_UPDATE;
import static kovalenko.vika.utils.AttributeConstant.DEFAULT_TAGS;
import static kovalenko.vika.utils.AttributeConstant.PRIORITIES;
import static kovalenko.vika.utils.AttributeConstant.STATUSES;
import static kovalenko.vika.utils.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.AttributeConstant.UPDATE;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@Slf4j
@Order(3)
@WebFilter(filterName = "TaskFilter", value = TODO_LINK)
public class TaskFilter implements Filter {
    private TaskService taskService;
    private TagService tagService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = filterConfig.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
        tagService = (TagService) context.getAttribute(TAG_SERVICE);

        log.debug("'TaskFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;

        if (isGetRequest(httpRequest)) {
            HttpSession session = httpRequest.getSession();
            checkPresenceOfTasksInSession(session);
            checkPresenceOfDefaultTagsInSession(session);

            chain.doFilter(request, response);
            return;
        }

        var httpResponse = (HttpServletResponse) response;
        String taskId = httpRequest.getParameter(UPDATE);

        if (nonNull(taskId)) {
            setRequestAttributesForUpdatingTask(httpRequest, Long.valueOf(taskId));

            httpRequest
                    .getServletContext()
                    .getRequestDispatcher(TASK_UPDATE.getValue())
                    .forward(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'TaskFilter' is destroyed");
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    private void checkPresenceOfTasksInSession(HttpSession session) {
        if (isNull(session.getAttribute(TASKS))) {
            var username = (String) session.getAttribute(USERNAME);
            List<TaskDTO> tasks = taskService.getAllUserTasks(username);
            session.setAttribute(TASKS, tasks);
        }
    }

    private void checkPresenceOfDefaultTagsInSession(HttpSession session){
        if (isNull(session.getAttribute(DEFAULT_TAGS))){
            session.setAttribute(DEFAULT_TAGS, tagService.getDefaultTags());
        }
    }

    private void setRequestAttributesForUpdatingTask(HttpServletRequest request, Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        request.setAttribute(TASK, task);
        request.setAttribute(PRIORITIES, TaskPriority.getAllPriorities());
        request.setAttribute(STATUSES, TaskStatus.getAllStatuses());
        request.setAttribute(TASK_TAGS, getTaskTagIds(task));
    }

    private String getTaskTagIds(TaskDTO task) {
        List<TagDTO> taskTags = task.getTags();
        StringBuilder taskTagIds = new StringBuilder();

        taskTags
                .forEach(tag -> taskTagIds.append(tag.getId()).append(" "));

        int indexOfLastSpace = taskTagIds.length() - 1;
        taskTagIds.deleteCharAt(indexOfLastSpace);

        return taskTagIds.toString();
    }
}
