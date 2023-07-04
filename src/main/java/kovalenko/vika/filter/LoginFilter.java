package kovalenko.vika.filter;

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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;
import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.LOGIN_LINK;

@Slf4j
@Order(2)
@WebFilter(filterName = "LoginFilter", value = LOGIN_LINK)
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'LoginFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!ServletUtil.isGetRequest(request) && forwardedDueToIncorrectData(request, response)) {
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean forwardedDueToIncorrectData(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        if (isNullOrBlank(username, password)) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

            log.warn("Unable to authorize: username or password is null/blank");

            request
                    .getServletContext()
                    .getRequestDispatcher(INDEX_JSP.getValue())
                    .forward(request, response);

            return true;
        }
        return false;
    }

    private boolean isNullOrBlank(String... targets) {
        for (String target : targets) {
            if (isNull(target) || target.isBlank()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'LoginFilter' is destroyed");
    }
}
