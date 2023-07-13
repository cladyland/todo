package kovalenko.vika.filter.task;

import kovalenko.vika.filter.AbstractFilterTest;
import kovalenko.vika.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ID;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewTaskFilterTest extends AbstractFilterTest {
    @Mock
    ServletContext context;
    @Mock
    UserService userService;
    NewTaskFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute(USER_SERVICE)).thenReturn(userService);

        filter = new NewTaskFilter();
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

        verify(request, never()).getSession();
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_not_get_request() throws ServletException, IOException {
        String username = "test-new-task";
        long id = 12345L;

        whenSession();
        when(request.getMethod()).thenReturn("post");
        when(session.getAttribute(USERNAME)).thenReturn(username);
        when(userService.getUserId(username)).thenReturn(id);

        doFilter(filter);

        verify(request, times(1)).setAttribute(USER_ID, id);
        verifyChainDoFilter();
    }
}
