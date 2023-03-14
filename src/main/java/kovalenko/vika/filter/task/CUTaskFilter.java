package kovalenko.vika.filter.task;

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
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebFilter(filterName = "CreateUpdateTaskFilter", urlPatterns = {NEW_TASK_LINK, TODO_LINK})
public class CUTaskFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(CUTaskFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'CreateUpdateTaskFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;

        if (isCreateTaskRequest(httpRequest) || isUpdateTaskRequest(httpRequest)) {
            String[] taskTagsIds = httpRequest.getParameterValues(TASK_TAGS);
            request.setAttribute(TASK_TAGS, convertIds(taskTagsIds));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'CreateUpdateTaskFilter' is destroyed");
    }

    private boolean isCreateTaskRequest(HttpServletRequest request) {
        return request.getRequestURI().equals(NEW_TASK_LINK);
    }

    private boolean isUpdateTaskRequest(HttpServletRequest request) {
        return nonNull(request.getParameter(SAVE_UPDATE));
    }

    private Set<Long> convertIds(String[] ids) {
        if (isNull(ids)) {
            return null;
        }
        return Arrays.stream(ids)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}
