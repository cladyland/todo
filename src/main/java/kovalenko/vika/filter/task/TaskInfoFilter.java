package kovalenko.vika.filter.task;

import lombok.extern.slf4j.Slf4j;

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

import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.AttributeConstant.COMMENT_ADD_TO_TASK;
import static kovalenko.vika.utils.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@Slf4j
@WebFilter(filterName = "TaskInfoFilter", value = TASK_INFO_LINK)
public class TaskInfoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'TaskInfoFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var currentSession = httpRequest.getSession();

        Long commentAdd = (Long) currentSession.getAttribute(COMMENT_ADD_TO_TASK);
        Long commentNotAdded = (Long) currentSession.getAttribute(TASK_ID);
        String moreInfoId = request.getParameter(MORE_INFO);
        Long taskId;

        if (nonNull(commentAdd)) {
            taskId = commentAdd;
            currentSession.removeAttribute(COMMENT_ADD_TO_TASK);
        } else if (nonNull(moreInfoId)) {
            taskId = Long.parseLong(moreInfoId);
        } else if (nonNull(commentNotAdded)) {
            taskId = commentNotAdded;
            currentSession.removeAttribute(TASK_ID);
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            log.warn("The request cannot be fulfilled: taskId is null");
            httpResponse.sendRedirect(NOT_FOUND_LINK);
            return;
        }

        request.setAttribute(TASK_ID, taskId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'TaskInfoFilter' is destroyed");
    }
}
