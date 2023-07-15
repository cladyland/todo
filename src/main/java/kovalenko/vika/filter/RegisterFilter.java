package kovalenko.vika.filter;

import kovalenko.vika.utils.AppMiddleware;
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
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.constants.AttributeConstant.ERRORS_MAP;
import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.REGISTER_LINK;

@Slf4j
@Order(3)
@WebFilter(filterName = "RegisterFilter", value = REGISTER_LINK)
public class RegisterFilter implements Filter {
    private static final String PRE_CHECK = "Pre-check of parameters for registration new user: status {}";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("'RegisterFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ServletUtil.isGetRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        var errors = new HashMap<String, String>();
        Map<String, String> parameters = createParametersMap(request);
        nullChecking(parameters, errors);

        if (paramsAreNotBlank(parameters, errors)) {
            request.setAttribute(PARAMETERS, parameters);
            log.info(PRE_CHECK, "OK (all necessary params for registration are not blank)");
            chain.doFilter(request, response);
        } else {
            setParamsToRequest(parameters, request);
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute(ERRORS_MAP, ServletUtil.convertToJson(errors));

            log.info(PRE_CHECK, "BAD (one or more parameters are blank)");

            request
                    .getServletContext()
                    .getRequestDispatcher(REGISTER.getValue())
                    .forward(request, response);
        }
    }

    private Map<String, String> createParametersMap(ServletRequest request) {
        var parameters = new HashMap<String, String>();
        parameters.put(FIRST_NAME, request.getParameter(FIRST_NAME));
        parameters.put(LAST_NAME, request.getParameter(LAST_NAME));
        parameters.put(USERNAME, request.getParameter(USERNAME));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        return parameters;
    }

    private void nullChecking(Map<String, String> parameters, Map<String, String> errors) {
        int countOfNullParams = 0;
        for (var parameter : parameters.entrySet()) {
            if (isNull(parameter.getValue())) {
                countOfNullParams++;
                log.warn("Parameter '{}' cannot be null", parameter.getKey());
                errors.put(parameter.getKey(), " cannot be null");
            }
        }
        if (!AppMiddleware.numberEqualsZero(countOfNullParams)) {
            throw new NullPointerException();
        }
    }

    private boolean paramsAreNotBlank(Map<String, String> parameters, Map<String, String> errors) {
        int countOfBlankParams = 0;
        for (var parameter : parameters.entrySet()) {
            if (parameter.getValue().isBlank()) {
                countOfBlankParams++;
                parameters.replace(parameter.getKey(), "");
                log.warn("Parameter '{}' cannot be blank", parameter.getKey());
                errors.put(parameter.getKey(), " cannot be blank");
            }
        }

        return AppMiddleware.numberEqualsZero(countOfBlankParams);
    }

    private void setParamsToRequest(Map<String, String> parameters, ServletRequest request) {
        request.setAttribute(FIRST_NAME, parameters.get(FIRST_NAME));
        request.setAttribute(LAST_NAME, parameters.get(LAST_NAME));
        request.setAttribute(USERNAME, parameters.get(USERNAME));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("'RegisterFilter' is destroyed");
    }
}
