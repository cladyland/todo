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

import static java.util.Objects.isNull;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_NOT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TASK_INFO_LINK;

@Slf4j
@WebFilter(filterName = "CommentFilter", value = COMMENT_LINK)
public class CommentFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'CommentFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpResponse = (HttpServletResponse) response;
        String taskId = request.getParameter(TASK_ID);

        if (isNullAndRedirected(taskId, TASK_ID, httpResponse)) {
            return;
        }

        String commentContest = request.getParameter(COMMENT);

        if (isNullAndRedirected(commentContest, COMMENT, httpResponse)) {
            return;
        }

        if (commentContest.isBlank()) {
            Long id = Long.parseLong(taskId);
            var currentSession = ((HttpServletRequest) request).getSession();

            currentSession.setAttribute(COMMENT_NOT_ADDED_TO_TASK, id);
            httpResponse.sendRedirect(TASK_INFO_LINK);

            log.warn("Failed to add comment to task '{}'. Comment contest cannot be blank", id);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isNullAndRedirected(Object target, String parameterName, HttpServletResponse response) throws IOException {
        if (isNull(target)) {
            log.warn("Parameter '{}' cannot be null", parameterName);
            response.sendRedirect(NOT_FOUND_LINK);
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'CommentFilter' is destroyed");
    }
}
