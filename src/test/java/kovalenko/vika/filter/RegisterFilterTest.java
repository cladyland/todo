package kovalenko.vika.filter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.constants.AttributeConstant.ERRORS_MAP;
import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterFilterTest extends AbstractFilterTest {
    final String TEST = "register-test";
    RegisterFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new RegisterFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter_when_get_request() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("get");

        doFilter(filter);

        verify(request, never()).getParameter(anyString());
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_post_request_and_valid_data() throws ServletException, IOException {
        var expected = createRequestParamsMap(TEST);

        when(request.getMethod()).thenReturn("post");
        setRulesForInputData(expected);

        doFilter(filter);

        verify(request, times(1)).setAttribute(PARAMETERS, expected);
        verifyChainDoFilter();
    }

    @Test
    void forward_when_post_request_and_blank_data() throws ServletException, IOException {
        var context = Mockito.mock(ServletContext.class);
        var dispatcher = Mockito.mock(RequestDispatcher.class);

        String expected = "{\"firstName\":\" cannot be blank\"," +
                "\"lastName\":\" cannot be blank\"," +
                "\"password\":\" cannot be blank\"," +
                "\"username\":\" cannot be blank\"}";

        when(request.getMethod()).thenReturn("post");
        setRulesForInputData(createRequestParamsMap(""));

        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(REGISTER.getValue())).thenReturn(dispatcher);

        doFilter(filter);

        verifyChainNeverDoFilter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(request, times(1)).setAttribute(ERRORS_MAP, expected);
        verify(dispatcher, times(1)).forward(request, response);
    }

    @Test
    void throws_exception_when_post_request_and_null_data() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("post");

        assertThrows(NullPointerException.class, () -> doFilter(filter));

        verifyChainNeverDoFilter();
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void setRulesForInputData(Map<String, String> params) {
        params.forEach((key, value) -> when(request.getParameter(key)).thenReturn(value));
    }

    private Map<String, String> createRequestParamsMap(String value) {
        return Map.of(FIRST_NAME, value,
                LAST_NAME, value,
                USERNAME, value,
                PASSWORD, value);
    }
}
