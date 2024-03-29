package kovalenko.vika.filter.task;

import kovalenko.vika.service.UserService;
import kovalenko.vika.utils.ServletUtil;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ID;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;

@Slf4j
@Order(2)
@WebFilter(filterName = "NewTaskFilter", value = NEW_TASK_LINK)
public class NewTaskFilter implements Filter {
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = filterConfig.getServletContext();
        userService = (UserService) context.getAttribute(USER_SERVICE);

        log.debug("'NewTaskFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!ServletUtil.isGetRequest(request)) {
            HttpSession session = ((HttpServletRequest) request).getSession();
            var username = (String) session.getAttribute(USERNAME);
            Long userId = userService.getUserId(username);

            request.setAttribute(USER_ID, userId);
            log.debug("Added userId '{}' to request", userId);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'NewTaskFilter' is destroyed");
    }
}
