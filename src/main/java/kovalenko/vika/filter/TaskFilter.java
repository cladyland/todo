package kovalenko.vika.filter;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.TaskService;

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

@WebFilter(filterName = "TaskFilter", value = "/todo")
public class TaskFilter implements Filter {
    private TaskService taskService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = filterConfig.getServletContext();
        taskService = (TaskService) context.getAttribute("taskService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var currentSession = httpRequest.getSession();

        if(isNull(currentSession.getAttribute("tasks"))){
            var username = (String) currentSession.getAttribute("username");
            List<TaskDTO> tasks = taskService.getAllUserTasks(username);
            currentSession.setAttribute("tasks", tasks);
        }

        String taskId = httpRequest.getParameter("update");
        if (nonNull(taskId)){
            TaskDTO task = taskService.getTaskById(Long.valueOf(taskId));
            httpRequest.setAttribute("task", task);
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
