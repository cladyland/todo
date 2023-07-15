package kovalenko.vika.servlet.task;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.exception.TaskException;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.servlet.AbstractServletTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static kovalenko.vika.enums.JSP.NEW_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASKS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ID;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewTaskServletTest extends AbstractServletTest {
    @Mock
    TaskService taskService;
    NewTaskServlet servlet;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        when(context.getAttribute(TASK_SERVICE)).thenReturn(taskService);
        servlet = new NewTaskServlet();
        super.init(servlet);
    }

    @Test
    void do_get_forward_to_new_task() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);
        verifyForward(NEW_TASK.getValue());
    }

    @Test
    void do_post_save_new_task_and_send_redirect() throws ServletException, IOException {
        var tasks = new ArrayList<>();

        setDoPostMockConditions();
        when(taskService.createTask(any(TaskCommand.class), eq(Set.of()))).thenReturn(Mockito.mock(TaskDTO.class));
        when(session.getAttribute(TASKS)).thenReturn(tasks);

        servlet.doPost(request, response);

        verify(session, times(1)).setAttribute(TASKS, tasks);
        verifyRedirect(TODO_LINK);
    }

    @Test
    void do_post_not_save_new_task_if_task_exception() throws ServletException, IOException {
        whenDispatcher();
        setDoPostMockConditions();
        when(taskService.createTask(any(TaskCommand.class), eq(Set.of()))).thenThrow(TaskException.class);

        servlet.doPost(request, response);

        verify(session, never()).setAttribute(TASKS, List.of());
        verify(response, never()).sendRedirect(TODO_LINK);
    }

    private void setDoPostMockConditions() {
        whenSession();
        when(request.getAttribute(TASK_TAGS)).thenReturn(Set.of());
        when(request.getAttribute(USER_ID)).thenReturn(1L);
        when(request.getParameter(PRIORITY)).thenReturn(TaskPriority.LOW.name());
        when(request.getParameter(STATUS)).thenReturn(TaskStatus.IN_PROGRESS.name());
    }
}
