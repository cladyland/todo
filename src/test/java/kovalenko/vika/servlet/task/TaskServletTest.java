package kovalenko.vika.servlet.task;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
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

import static kovalenko.vika.enums.JSP.TASK_UPDATE;
import static kovalenko.vika.enums.JSP.TODO;
import static kovalenko.vika.utils.constants.AttributeConstant.DELETE;
import static kovalenko.vika.utils.constants.AttributeConstant.DESCRIPTION;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITY;
import static kovalenko.vika.utils.constants.AttributeConstant.SAVE_UPDATE;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASKS;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;
import static kovalenko.vika.utils.constants.AttributeConstant.TITLE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServletTest extends AbstractServletTest {
    final String TEST = "task-test";
    final String S_ID = "27";
    final long L_ID = 27L;
    @Mock
    TaskService taskService;
    TaskServlet servlet;

    @BeforeEach
    void init() throws ServletException {
        when(context.getAttribute(TASK_SERVICE)).thenReturn(taskService);
        servlet = new TaskServlet();
        super.init(servlet);
    }

    @Test
    void do_get_forward_to_todo() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);
        verifyForward(TODO.getValue());
    }

    @Test
    void do_post_delete_task() throws ServletException, IOException {
        List<TaskDTO> tasks = new ArrayList<>();
        tasks.add(TaskDTO.builder().id(10L).build());
        tasks.add(TaskDTO.builder().id(L_ID).build());

        whenSession();
        whenDispatcher();
        when(request.getParameter(DELETE)).thenReturn(S_ID);
        when(session.getAttribute(TASKS)).thenReturn(tasks);

        servlet.doPost(request, response);

        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getId(), 10L);

        verify(taskService, times(1)).deleteTask(L_ID);
        verify(session, times(1)).setAttribute(TASKS, tasks);
        verifyForward(TODO.getValue());
    }

    @Test
    void do_post_update_task() throws ServletException, IOException {
        whenSession();
        whenDispatcher();
        when(request.getParameter(DELETE)).thenReturn(null);
        when(request.getParameter(SAVE_UPDATE)).thenReturn(S_ID);
        when(request.getAttribute(TITLE)).thenReturn(TEST);
        when(request.getAttribute(DESCRIPTION)).thenReturn(TEST);
        when(request.getParameter(PRIORITY)).thenReturn(TaskPriority.MEDIUM.name());
        when(request.getParameter(STATUS)).thenReturn(TaskStatus.PLANNED.name());
        when(request.getAttribute(TASK_TAGS)).thenReturn(Set.of());
        when(session.getAttribute(USER_ATTR)).thenReturn(Mockito.mock(UserDTO.class));

        servlet.doPost(request, response);

        verify(taskService, times(1)).updateTask(any(TaskDTO.class), eq(Set.of()));
        verifyForward(TODO.getValue());
    }

    @Test
    void do_post_update_exception_forward_to_update() throws ServletException, IOException {
        whenDispatcher();
        when(request.getParameter(DELETE)).thenReturn(null);
        when(request.getParameter(SAVE_UPDATE)).thenReturn(S_ID);
        when(request.getAttribute(TITLE)).thenThrow(TaskException.class);
        when(taskService.getTaskById(anyLong())).thenReturn(Mockito.mock(TaskDTO.class));

        servlet.doPost(request, response);

        verifyForward(TASK_UPDATE.getValue());
    }
}
