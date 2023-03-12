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

import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.AttributeConstant.COMMENT_ADD_TO_TASK;
import static kovalenko.vika.utils.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@WebFilter(filterName = "TaskInfoFilter", value = TASK_INFO_LINK)
public class TaskInfoFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(TaskInfoFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'TaskInfoFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var currentSession = httpRequest.getSession();

        Long commentAdd = (Long) currentSession.getAttribute(COMMENT_ADD_TO_TASK);
        String moreInfo = request.getParameter(MORE_INFO);
        Long taskId;

        if (nonNull(commentAdd)) {
            taskId = commentAdd;
            currentSession.removeAttribute(COMMENT_ADD_TO_TASK);
        } else if (nonNull(moreInfo)) {
            taskId = Long.parseLong(moreInfo);
        } else {
            ((HttpServletResponse) response).sendRedirect(NOT_FOUND_LINK);
            return;
        }

        request.setAttribute(TASK_ID, taskId);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'TaskInfoFilter' is destroyed");
    }
}
