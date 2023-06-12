package kovalenko.vika.filter;

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
import static kovalenko.vika.utils.AttributeConstant.COMMENT;
import static kovalenko.vika.utils.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@Slf4j
@WebFilter(filterName = "CommentFilter", value = COMMENT_LINK)
public class CommentFilter implements Filter {
    private static final String PARAMETER_CANNOT_BE_NULL = "Parameter '{}' cannot be null";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'CommentFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpResponse = (HttpServletResponse) response;
        String taskId = request.getParameter(TASK_ID);

        if (isNull(taskId)) {
            notFoundRedirect(httpResponse, TASK_ID);
            return;
        }

        String commentContest = request.getParameter(COMMENT);

        if (isNull(commentContest)) {
            notFoundRedirect(httpResponse, COMMENT);
            return;
        } else if (commentContest.isBlank()) {
            Long id = Long.parseLong(taskId);
            var currentSession = ((HttpServletRequest) request).getSession();

            currentSession.setAttribute(TASK_ID, id);
            httpResponse.sendRedirect(TASK_INFO_LINK);

            log.warn("Failed to add comment to task '{}'. Comment contest cannot be blank", id);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'CommentFilter' is destroyed");
    }

    private void notFoundRedirect(HttpServletResponse response, String parameterName) throws IOException {
        log.warn(PARAMETER_CANNOT_BE_NULL, parameterName);
        response.sendRedirect(NOT_FOUND_LINK);
    }
}
