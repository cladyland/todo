package kovalenko.vika.filter.security;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.LOGOUT_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthFilterTest extends AbstractFilterTest {
    AuthFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new AuthFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        whenSession();
        when(request.getRequestURI()).thenReturn("uri");
        when(session.getAttribute(USERNAME)).thenReturn(null);

        doFilter(filter);
        verifyChainDoFilter();
    }

    @Test
    void when_logout_redirect_to_login() throws ServletException, IOException {
        whenSession();
        when(request.getRequestURI()).thenReturn(LOGOUT_LINK);

        doFilter(filter);

        verify(session, times(1)).invalidate();
        verifyChainNeverDoFilter();
    }

    @Test
    void redirect_when_username_not_null() throws ServletException, IOException {
        whenSession();
        when(request.getRequestURI()).thenReturn("uri");
        when(session.getAttribute(USERNAME)).thenReturn("user-test");

        doFilter(filter);

        verify(response, times(1)).sendRedirect(TODO_LINK);
        verifyChainNeverDoFilter();
    }
}
