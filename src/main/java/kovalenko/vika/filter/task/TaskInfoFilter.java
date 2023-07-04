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
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_NOT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TASK_INFO_LINK;

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

        Long taskId = determineTaskId(httpRequest, httpResponse);

        if (isNull(taskId)) {
            log.warn("The request cannot be fulfilled: taskId is null");
            httpResponse.sendRedirect(NOT_FOUND_LINK);
            return;
        }

        request.setAttribute(TASK_ID, taskId);
        chain.doFilter(request, response);
    }

    private Long determineTaskId(HttpServletRequest request, HttpServletResponse response) {
        var currentSession = request.getSession();

        String moreInfoId = request.getParameter(MORE_INFO);
        Long commentAdded = (Long) currentSession.getAttribute(COMMENT_ADDED_TO_TASK);
        Long commentNotAdded = (Long) currentSession.getAttribute(COMMENT_NOT_ADDED_TO_TASK);

        Long taskId = null;

        if (nonNull(moreInfoId)) {
            taskId = Long.parseLong(moreInfoId);
        } else if (nonNull(commentAdded)) {
            taskId = commentAdded;
            removeSessionAttribute(currentSession, COMMENT_ADDED_TO_TASK);
        } else if (nonNull(commentNotAdded)) {
            taskId = commentNotAdded;
            removeSessionAttribute(currentSession, COMMENT_NOT_ADDED_TO_TASK);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return taskId;
    }

    private void removeSessionAttribute(HttpSession session, String attribute) {
        session.removeAttribute(attribute);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'TaskInfoFilter' is destroyed");
    }
}
