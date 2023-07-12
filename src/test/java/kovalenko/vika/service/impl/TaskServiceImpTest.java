package kovalenko.vika.service.impl;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.Verifier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImpTest {
    final long ID = 10L;
    @Mock
    TaskDAO taskDAO;
    @Mock
    TagDAO tagDAO;
    @Mock
    Session session;
    @Mock
    Task taskMock;
    TaskServiceImp service;

    @BeforeEach
    void init(TestInfo info) {
        service = new TaskServiceImp(taskDAO, tagDAO);

        if (isTestExclusion(info)) return;

        when(taskDAO.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(Mockito.mock(Transaction.class));

    }

    private boolean isTestExclusion(TestInfo info) {
        String method = info.getDisplayName();
        return method.equals("get_priorities()") || method.equals("get_statuses()");
    }

    @Test
    void get_task_by_id() {
        when(taskDAO.getById(anyLong(), eq(session))).thenReturn(taskMock);
        when(taskMock.getPriority()).thenReturn(TaskPriority.LOW);
        when(taskMock.getStatus()).thenReturn(TaskStatus.SUSPENDED);

        service.getTaskById(ID);

        Verifier.transactionBegins(session);
        verify(taskDAO, times(1)).getById(ID, session);
        Verifier.transactionCommitted(session);
    }

    @Test
    void get_task_by_id_return_null_if_task_not_exist() {
        when(taskDAO.getById(anyLong(), eq(session))).thenReturn(null);

        assertNull(service.getTaskById(ID));

        Verifier.transactionBegins(session);
        verify(taskDAO, times(1)).getById(ID, session);
        Verifier.transactionNotCommitted(session);
    }

    @Test
    void get_all_user_tasks() {
        String username = "test";
        when(taskDAO.getAllUserTasks(anyString(), eq(session))).thenReturn(new ArrayList<>());

        service.getAllUserTasks(username);

        Verifier.transactionBegins(session);
        verify(taskDAO, times(1)).getAllUserTasks(username, session);
        Verifier.transactionCommitted(session);
    }

    @Test
    void get_priorities() {
        List<TaskPriority> expected = TaskPriority.getAllPriorities();

        assertEquals(expected, service.getPriorities());
    }

    @Test
    void get_statuses() {
        List<TaskStatus> expected = TaskStatus.getAllStatuses();

        assertEquals(expected, service.getStatuses());
    }

    @Test
    void create_new_task() {
        var task = TaskCommand.builder()
                .title("title")
                .description("description")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PLANNED)
                .build();

        service.createTask(task, null);

        Verifier.transactionBegins(session);
        verify(tagDAO, times(1)).getTagsByIds(null);
        verify(taskDAO, times(1)).save(any(Task.class));
        Verifier.transactionCommitted(session);
    }

    @Test
    void update_task() {
        TaskPriority priority = TaskPriority.MEDIUM;
        TaskStatus status = TaskStatus.DONE;
        var task = TaskDTO.builder()
                .id(ID)
                .title("title")
                .description("description")
                .priority(priority.getValue())
                .status(status.getValue())
                .tags(null)
                .build();

        when(taskDAO.getById(anyLong(), eq(session))).thenReturn(taskMock);

        service.updateTask(task, null);

        Verifier.transactionBegins(session);
        verify(taskDAO, times(1)).getById(task.getId(), session);

        verify(taskMock, times(1)).setTitle(task.getTitle());
        verify(taskMock, times(1)).setDescription(task.getDescription());
        verify(taskMock, times(1)).setStatus(status);
        verify(taskMock, times(1)).setPriority(priority);
        verify(taskMock, times(1)).setTags(Set.of());

        verify(tagDAO, times(1)).getTagsByIds(null);
        verify(taskDAO, times(1)).update(any(Task.class));
        Verifier.transactionCommitted(session);
    }

    @Test
    void delete_task() {
        when(taskDAO.delete(anyLong(), eq(session))).thenReturn(taskMock);
        when(taskMock.getPriority()).thenReturn(TaskPriority.LOW);
        when(taskMock.getStatus()).thenReturn(TaskStatus.SUSPENDED);

        service.deleteTask(ID);

        Verifier.transactionBegins(session);
        verify(taskDAO, times(1)).delete(ID, session);
        Verifier.transactionCommitted(session);
    }
}
