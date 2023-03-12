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
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.LOGIN_LINK;
import static kovalenko.vika.utils.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {LOGIN_LINK, REGISTER_LINK})
public class AuthFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'AuthorizationFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        if (nonNull(httpRequest.getSession().getAttribute(USERNAME))) {
            httpResponse.sendRedirect(TODO_LINK);
            LOG.warn("The user is already authorized");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'AuthorizationFilter' is destroyed");
    }
}
