package kovalenko.vika.filter.task;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_NOT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.MORE_INFO;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.LinkConstant.NOT_FOUND_LINK;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskInfoFilterTest extends AbstractFilterTest {
    TaskInfoFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        whenSession();
        filter = new TaskInfoFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        when(request.getParameter(MORE_INFO)).thenReturn("1");

        doFilter(filter);

        verify(request, times(1)).setAttribute(TASK_ID, 1L);
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter2() throws ServletException, IOException {
        when(session.getAttribute(COMMENT_ADDED_TO_TASK)).thenReturn(1L);

        doFilter(filter);

        verify(request, times(1)).setAttribute(TASK_ID, 1L);
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter3() throws ServletException, IOException {
        when(session.getAttribute(COMMENT_ADDED_TO_TASK)).thenReturn(null);
        when(session.getAttribute(COMMENT_NOT_ADDED_TO_TASK)).thenReturn(1L);

        doFilter(filter);

        verify(request, times(1)).setAttribute(TASK_ID, 1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyChainDoFilter();
    }

    @Test
    void redirect_when_null_task_id() throws ServletException, IOException {
        doFilter(filter);

        verify(response, times(1)).sendRedirect(NOT_FOUND_LINK);
        verify(request, never()).setAttribute(TASK_ID, eq(anyLong()));
        verifyChainNeverDoFilter();
    }
}
