package kovalenko.vika.filter.task;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_NOT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.LinkConstant.NOT_FOUND_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TASK_INFO_LINK;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentFilterTest extends AbstractFilterTest {
    final String ID = "123";
    CommentFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new CommentFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        when(request.getParameter(TASK_ID)).thenReturn(ID);
        when(request.getParameter(COMMENT)).thenReturn("contest");

        doFilter(filter);

        verify(response, never()).sendRedirect(anyString());
        verifyChainDoFilter();
    }

    @Test
    void not_found_redirect_when_null_task_id() throws ServletException, IOException {
        when(request.getParameter(TASK_ID)).thenReturn(null);

        doFilter(filter);

        verify(response, times(1)).sendRedirect(NOT_FOUND_LINK);
        verify(request, never()).getParameter(COMMENT);
        verifyChainNeverDoFilter();
    }

    @Test
    void not_found_redirect_when_null_comment_contest() throws ServletException, IOException {
        when(request.getParameter(TASK_ID)).thenReturn(ID);
        when(request.getParameter(COMMENT)).thenReturn(null);

        doFilter(filter);

        verify(response, times(1)).sendRedirect(NOT_FOUND_LINK);
        verifyChainNeverDoFilter();
    }

    @Test
    void task_info_redirect_when_blank_comment_contest() throws ServletException, IOException {
        whenSession();
        when(request.getParameter(TASK_ID)).thenReturn(ID);
        when(request.getParameter(COMMENT)).thenReturn("");

        doFilter(filter);

        verify(session, times(1)).setAttribute(COMMENT_NOT_ADDED_TO_TASK, Long.parseLong(ID));
        verify(response, times(1)).sendRedirect(TASK_INFO_LINK);
        verifyChainNeverDoFilter();
    }
}
