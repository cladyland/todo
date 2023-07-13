package kovalenko.vika.servlet.task;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.servlet.AbstractServletTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletException;

import java.io.IOException;

import static kovalenko.vika.enums.JSP.TASK_INFO;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskInfoServletTest extends AbstractServletTest {
    @Mock
    TaskService taskService;
    TaskInfoServlet servlet;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        when(context.getAttribute(TASK_SERVICE)).thenReturn(taskService);
        servlet = new TaskInfoServlet();
        super.init(servlet);
    }

    @Test
    void do_get_forward_to_task_info() throws ServletException, IOException {
        var task = Mockito.mock(TaskDTO.class);

        whenDispatcher();
        when(request.getAttribute(TASK_ID)).thenReturn(14L);
        when(taskService.getTaskById(anyLong())).thenReturn(task);

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute(TASK, task);
        verifyForward(TASK_INFO.getValue());
    }
}
