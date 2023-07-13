package kovalenko.vika.filter.task;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static kovalenko.vika.utils.constants.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CUTaskFilterTest extends AbstractFilterTest {
    CUTaskFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new CUTaskFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(TODO_LINK);

        doFilter(filter);

        verify(request, never()).getParameterValues(TASK_TAGS);
        verifyChainDoFilter();
    }

    @Test
    void do_filter_when_create_request_and_null_tags() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(NEW_TASK_LINK);

        doFilter(filter);

        verify(request, times(1)).getParameterValues(TASK_TAGS);
        verify(request, never()).setAttribute(TASK_TAGS, eq(any()));
        verifyChainDoFilter();
    }

    @Test
    void do_filter_when_update_request_and_not_null_tags() throws ServletException, IOException {
        var expected = new TreeSet<>(Set.of(1L, 2L, 3L));

        when(request.getRequestURI()).thenReturn(TODO_LINK);
        when(request.getParameter(SAVE_UPDATE)).thenReturn("update");
        when(request.getParameterValues(TASK_TAGS)).thenReturn(new String[]{"1", "2", "3"});

        doFilter(filter);

        verify(request, times(1)).setAttribute(TASK_TAGS, expected);
        verifyChainDoFilter();
    }
}
