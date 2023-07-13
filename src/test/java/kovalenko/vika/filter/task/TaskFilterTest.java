package kovalenko.vika.filter.task;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.filter.AbstractFilterTest;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.TASK_UPDATE;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.UPDATE;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskFilterTest extends AbstractFilterTest {
    @Mock
    ServletContext context;
    @Mock
    TaskService taskService;
    @Mock
    TagService tagService;
    TaskFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute(TASK_SERVICE)).thenReturn(taskService);
        when(context.getAttribute(TAG_SERVICE)).thenReturn(tagService);

        filter = new TaskFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter_when_get_request() throws ServletException, IOException {
        whenSession();
        when(request.getMethod()).thenReturn("get");

        doFilter(filter);

        verifyChainDoFilter();
        verify(request, never()).getParameter(UPDATE);
    }

    @Test
    void forward_when_request_for_update() throws ServletException, IOException {
        var dispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("post");
        when(request.getParameter(UPDATE)).thenReturn("812");
        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(TASK_UPDATE.getValue())).thenReturn(dispatcher);
        when(taskService.getTaskById(812L)).thenReturn(Mockito.mock(TaskDTO.class));

        doFilter(filter);

        verify(dispatcher, times(1)).forward(request, response);
        verifyChainNeverDoFilter();
    }

    @Test
    void chain_do_filter_when_not_get_and_not_update() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("post");

        doFilter(filter);

        assertNull(request.getParameter(UPDATE));
        verify(request, never()).getSession();
        verifyChainDoFilter();
    }
}
