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

import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginFilterTest extends AbstractFilterTest {
    final String TEST = "login-test";
    LoginFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new LoginFilter();
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

        verify(request, never()).getParameter(USERNAME);
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_post_request() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("post");
        when(request.getParameter(USERNAME)).thenReturn(TEST);
        when(request.getParameter(PASSWORD)).thenReturn(TEST);

        doFilter(filter);

        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyChainDoFilter();
    }

    @Test
    void forward_when_post_request_and_invalid_data() throws ServletException, IOException {
        var context = Mockito.mock(ServletContext.class);
        var dispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("post");
        when(request.getParameter(USERNAME)).thenReturn("");
        when(request.getParameter(PASSWORD)).thenReturn("");
        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(INDEX_JSP.getValue())).thenReturn(dispatcher);

        doFilter(filter);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher, times(1)).forward(request, response);
        verifyChainNeverDoFilter();
    }
}
