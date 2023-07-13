package kovalenko.vika.filter.task;

import kovalenko.vika.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.constants.AttributeConstant.BACK_BUTTON_LINK;
import static kovalenko.vika.utils.constants.AttributeConstant.BACK_BUTTON_TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.FROM_TODO;
import static kovalenko.vika.utils.constants.AttributeConstant.TO_CREATE_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TO_TASKS_LIST;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TAG_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebFilter(filterName = "TagFilter", value = TAG_LINK)
public class TagFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'TagFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ServletUtil.isGetRequest(request)) {
            var session = ((HttpServletRequest) request).getSession();
            boolean isTodoRequest = nonNull(request.getParameter(FROM_TODO));

            String link = isTodoRequest ? TODO_LINK : NEW_TASK_LINK;
            String title = isTodoRequest ? TO_TASKS_LIST : TO_CREATE_TASK;

            session.setAttribute(BACK_BUTTON_LINK, link);
            session.setAttribute(BACK_BUTTON_TITLE, title);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'TagFilter' is destroyed");
    }
}
