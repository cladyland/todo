package kovalenko.vika.filter.security;

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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.LOGIN_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.LOGOUT_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@Order(1)
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {LOGIN_LINK, REGISTER_LINK, LOGOUT_LINK})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'AuthorizationFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().equals(LOGOUT_LINK)) {
            httpRequest.getSession().invalidate();
            httpResponse.sendRedirect(LOGIN_LINK);
            return;
        }

        if (nonNull(httpRequest.getSession().getAttribute(USERNAME))) {
            httpResponse.sendRedirect(TODO_LINK);
            log.warn("Unable to access the registration or authorization page: the user is already authorized");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'AuthorizationFilter' is destroyed");
    }
}
