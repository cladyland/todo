package kovalenko.vika.filter;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kovalenko.vika.enums.JSP.TASK_UPDATE;
import static kovalenko.vika.utils.AttributeConstant.TASK;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.UPDATE;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebFilter(filterName = "TaskFilter", value = TODO_LINK)
public class TaskFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(TaskFilter.class);
    private TaskService taskService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = filterConfig.getServletContext();
        taskService = (TaskService) context.getAttribute(TASK_SERVICE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var currentSession = httpRequest.getSession();

        if(isNull(currentSession.getAttribute(TASKS))){
            var username = (String) currentSession.getAttribute(USERNAME);
            List<TaskDTO> tasks = taskService.getAllUserTasks(username);
            currentSession.setAttribute(TASKS, tasks);
        }

        String taskId = httpRequest.getParameter(UPDATE);
        if (nonNull(taskId)){
            TaskDTO task = taskService.getTaskById(Long.valueOf(taskId));
            httpRequest.setAttribute(TASK, task);
            httpRequest
                    .getServletContext()
                    .getRequestDispatcher(TASK_UPDATE.getValue())
                    .forward(httpRequest, httpResponse);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
