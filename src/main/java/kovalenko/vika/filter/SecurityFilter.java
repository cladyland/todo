package kovalenko.vika.filter;

import kovalenko.vika.dto.TaskDTO;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.AttributeConstant.TASKS;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.LinkConstant.SECURITY_LINK;

@WebFilter(filterName = "SecurityFilter", value = SECURITY_LINK)
public class SecurityFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'SecurityFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var currentSession = httpRequest.getSession();
        var username = (String) currentSession.getAttribute(USERNAME);

        if (isNull(username)) {
            String uri = httpRequest.getRequestURI();
            LOG.warn("Access to '{}' is denied: authorization is required", uri);
            httpResponse.sendRedirect("/");
        } else {
            String moreInfoId = request.getParameter(MORE_INFO);
            if (nonNull(moreInfoId)) {
                boolean accessIsAllowed = checkAccessRights(currentSession, Long.parseLong(moreInfoId));
                if (!accessIsAllowed) {
                    LOG.warn("User '{}' has no access to task '{}'", username, moreInfoId);
                    httpResponse.sendRedirect(NOT_FOUND_LINK);
                    return;
                }
            }

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'SecurityFilter' is destroyed");
    }

    private boolean checkAccessRights(HttpSession session, Long taskId) {
        var userTasks = (List<TaskDTO>) session.getAttribute(TASKS);
        TaskDTO userTask = null;

        if (nonNull(userTasks)) {
            userTask = userTasks
                    .stream()
                    .filter(taskDTO -> taskDTO.getId().equals(taskId))
                    .findFirst()
                    .orElse(null);
        }

        return nonNull(userTask);
    }
}