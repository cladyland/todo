package kovalenko.vika.filter.security;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static kovalenko.vika.utils.constants.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.constants.AttributeConstant.TASKS;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.LOGIN_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.NOT_FOUND_LINK;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityFilterTest extends AbstractFilterTest {
    final String TEST = "test-username";
    SecurityFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        whenSession();
        filter = new SecurityFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        when(session.getAttribute(USERNAME)).thenReturn(TEST);

        doFilter(filter);
        verify(response, never()).sendRedirect(anyString());
        verifyChainDoFilter();
    }

    @Test
    void when_null_username_redirect_to_login() throws ServletException, IOException {
        when(session.getAttribute(USERNAME)).thenReturn(null);

        doFilter(filter);

        verify(response, times(1)).sendRedirect(LOGIN_LINK);
        verifyChainNeverDoFilter();
    }

    @Test
    void not_found_redirect_when_user_has_no_access_to_task() throws ServletException, IOException {
        when(session.getAttribute(USERNAME)).thenReturn(TEST);
        when(request.getParameter(MORE_INFO)).thenReturn("24");
        when(session.getAttribute(TASKS)).thenReturn(List.of());

        doFilter(filter);

        verify(response, times(1)).sendRedirect(NOT_FOUND_LINK);
        verifyChainNeverDoFilter();
    }
}
