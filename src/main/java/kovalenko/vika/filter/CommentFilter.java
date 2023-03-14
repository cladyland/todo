package kovalenko.vika.filter;

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

import static java.util.Objects.isNull;
import static kovalenko.vika.utils.AttributeConstant.COMMENT;
import static kovalenko.vika.utils.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@WebFilter(filterName = "CommentFilter", value = COMMENT_LINK)
public class CommentFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(CommentFilter.class);
    private static final String PARAMETER_CANNOT_BE_NULL = "Parameter '{}' cannot be null";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'CommentFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpResponse = (HttpServletResponse) response;
        String taskId = request.getParameter(TASK_ID);

        if (isNull(taskId)) {
            notfoundRedirect(httpResponse, TASK_ID);
            return;
        }

        String commentContest = request.getParameter(COMMENT);

        if (isNull(commentContest)) {
            notfoundRedirect(httpResponse, COMMENT);
            return;
        } else if (commentContest.isBlank()) {
            Long id = Long.parseLong(taskId);
            var currentSession = ((HttpServletRequest) request).getSession();

            currentSession.setAttribute(TASK_ID, id);
            httpResponse.sendRedirect(TASK_INFO_LINK);

            LOG.warn("Failed to add comment to task '{}'. Comment contest cannot be blank", id);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'CommentFilter' is destroyed");
    }

    private void notfoundRedirect(HttpServletResponse response, String parameterName) throws IOException {
        LOG.warn(PARAMETER_CANNOT_BE_NULL, parameterName);
        response.sendRedirect(NOT_FOUND_LINK);
    }
}
