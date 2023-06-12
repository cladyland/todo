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

import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.LOGIN_LINK;

@Slf4j
@WebFilter(filterName = "LoginFilter", value = LOGIN_LINK)
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'LoginFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;

        if (isGetRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        var httpResponse = (HttpServletResponse) response;

        String username = httpRequest.getParameter(USERNAME);
        String password = httpRequest.getParameter(PASSWORD);

        if (username.isBlank() || password.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.warn("Unable to authorize. Username or password is blank");

            httpRequest
                    .getServletContext()
                    .getRequestDispatcher(INDEX_JSP.getValue())
                    .forward(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'LoginFilter' is destroyed");
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }
}
