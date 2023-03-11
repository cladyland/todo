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
import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.LOGIN_LINK;

@WebFilter(filterName = "LoginFilter", value = LOGIN_LINK)
public class LoginFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.info("'LoginFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        String username = httpRequest.getParameter(USERNAME);
        String password = httpRequest.getParameter(PASSWORD);

        if (isNull(username)) {
            forwardToIndex(httpRequest, httpResponse);
            return;
        }

        if (username.isBlank() || password.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forwardToIndex(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("'LoginFilter' is destroyed");
    }

    private void forwardToIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request
                .getServletContext()
                .getRequestDispatcher(INDEX_JSP.getValue())
                .forward(request, response);
    }
}