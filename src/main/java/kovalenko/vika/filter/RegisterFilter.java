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
import java.util.HashMap;

import static java.util.Objects.isNull;
import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.LinkConstant.REGISTER_LINK;

@WebFilter(filterName = "RegisterFilter", value = REGISTER_LINK)
public class RegisterFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterFilter.class);
    private static final String PRE_CHECK = "Pre-check of parameters for registration new user: status {}";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.debug("'RegisterFilter' initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;

        if (isGetRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        var httpResponse = (HttpServletResponse) response;

        HashMap<String, String> parameters = createParametersMap(httpRequest);
        nullChecking(parameters);

        if (paramsAreNotBlank(parameters)) {
            httpRequest.setAttribute(PARAMETERS, parameters);
            LOG.info(PRE_CHECK, "OK");
            chain.doFilter(request, response);
        } else {
            setParamsToRequest(parameters, httpRequest);
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            LOG.info(PRE_CHECK, "BAD (one or more parameters are blank)");

            httpRequest
                    .getServletContext()
                    .getRequestDispatcher(REGISTER.getValue())
                    .forward(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.debug("'RegisterFilter' is destroyed");
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    private void setParamsToRequest(HashMap<String, String> parameters, HttpServletRequest request) {
        request.setAttribute(FIRST_NAME, parameters.get(FIRST_NAME));
        request.setAttribute(LAST_NAME, parameters.get(LAST_NAME));
        request.setAttribute(USERNAME, parameters.get(USERNAME));
    }

    private void nullChecking(HashMap<String, String> parameters) {
        int countOfNullParams = 0;
        for (var parameter : parameters.entrySet()) {
            if (isNull(parameter.getValue())) {
                countOfNullParams++;
                LOG.warn("Parameter '{}' cannot be null", parameter.getKey());
            }
        }
        if (countOfNullParams > 0) {
            throw new NullPointerException();
        }
    }

    private boolean paramsAreNotBlank(HashMap<String, String> parameters) {
        int countOfBlankParams = 0;
        for (var parameter : parameters.entrySet()) {
            if (parameter.getValue().isBlank()) {
                countOfBlankParams++;
                parameters.replace(parameter.getKey(), "");
                LOG.warn("Parameter '{}' cannot be blank", parameter.getKey());
            }
        }
        return countOfBlankParams == 0;
    }

    private HashMap<String, String> createParametersMap(HttpServletRequest request) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(FIRST_NAME, request.getParameter(FIRST_NAME));
        parameters.put(LAST_NAME, request.getParameter(LAST_NAME));
        parameters.put(USERNAME, request.getParameter(USERNAME));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        return parameters;
    }
}
