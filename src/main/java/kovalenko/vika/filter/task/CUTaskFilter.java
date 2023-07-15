package kovalenko.vika.filter.task;

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
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.constants.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@Order(1)
@WebFilter(filterName = "CreateUpdateTaskFilter", urlPatterns = {NEW_TASK_LINK, TODO_LINK})
public class CUTaskFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'CreateUpdateTaskFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isCreateTaskRequest(request) || isUpdateTaskRequest(request)) {
            String[] taskTagsIds = request.getParameterValues(TASK_TAGS);
            if (nonNull(taskTagsIds)) {
                request.setAttribute(TASK_TAGS, convertIds(taskTagsIds));
                log.debug("Added tag ids {} to request for create or update task", Arrays.toString(taskTagsIds));
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isCreateTaskRequest(ServletRequest request) {
        return ((HttpServletRequest) request).getRequestURI().equals(NEW_TASK_LINK);
    }

    private boolean isUpdateTaskRequest(ServletRequest request) {
        return nonNull(request.getParameter(SAVE_UPDATE));
    }

    private Set<Long> convertIds(String[] ids) {
        return Arrays.stream(ids)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'CreateUpdateTaskFilter' is destroyed");
    }
}
