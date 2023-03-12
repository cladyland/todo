package kovalenko.vika.filter;

import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.isNull;
import static kovalenko.vika.utils.AttributeConstant.TAGS;
import static kovalenko.vika.utils.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_ID;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USER_TAGS;
import static kovalenko.vika.utils.LinkConstant.NEW_TASK_LINK;

@WebFilter(filterName = "NewTaskFilter", value = NEW_TASK_LINK)
public class NewTaskFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(NewTaskFilter.class);
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = filterConfig.getServletContext();
        tagService = (TagService) context.getAttribute(TAG_SERVICE);
        userService = (UserService) context.getAttribute(USER_SERVICE);

        LOG.debug("'NewTaskFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        var username = (String) session.getAttribute(USERNAME);
        Long userId = userService.getUserId(username);

        if (isGetRequest(httpRequest)) {
            if (isNull(session.getAttribute(TAGS))) {
                session.setAttribute(TAGS, tagService.getDefaultTags());
            }
            if (isNull(session.getAttribute(USER_TAGS))) {
                session.setAttribute(USER_TAGS, tagService.getUserTags(userId));
            }
            chain.doFilter(request, response);
            return;
        }

        request.setAttribute(USER_ID, userId);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'NewTaskFilter' is destroyed");
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }
}
