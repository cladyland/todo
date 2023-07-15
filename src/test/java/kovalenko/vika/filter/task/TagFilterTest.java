package kovalenko.vika.filter.task;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;

import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.BACK_BUTTON_LINK;
import static kovalenko.vika.utils.constants.AttributeConstant.BACK_BUTTON_TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.FROM_TODO;
import static kovalenko.vika.utils.constants.AttributeConstant.TO_CREATE_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TO_TASKS_LIST;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagFilterTest extends AbstractFilterTest {
    TagFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new TagFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter_when_not_get_request() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("post");

        doFilter(filter);

        verify(request, never()).getSession();
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_get_request_from_todo() throws ServletException, IOException {
        whenSession();
        when(request.getMethod()).thenReturn("get");
        when(request.getParameter(FROM_TODO)).thenReturn("");

        doFilter(filter);

        verify(session, times(1)).setAttribute(BACK_BUTTON_LINK, TODO_LINK);
        verify(session, times(1)).setAttribute(BACK_BUTTON_TITLE, TO_TASKS_LIST);
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_get_request() throws ServletException, IOException {
        whenSession();
        when(request.getMethod()).thenReturn("get");
        when(request.getParameter(FROM_TODO)).thenReturn(null);

        doFilter(filter);

        verify(session, times(1)).setAttribute(BACK_BUTTON_LINK, NEW_TASK_LINK);
        verify(session, times(1)).setAttribute(BACK_BUTTON_TITLE, TO_CREATE_TASK);
        verifyChainDoFilter();
    }
}
