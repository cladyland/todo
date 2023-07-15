package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.AbstractDAOTest;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kovalenko.vika.dao.H2Env.LAST_ID_TASKS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskDAOImpTest extends AbstractDAOTest {
    private long expId;
    private long expUserId;
    private String expTitle;
    private TaskPriority expPriority;
    private TaskStatus expStatus;
    TaskDAOImp dao;

    @Override
    @BeforeEach
    protected void init() {
        dao = new TaskDAOImp(factory);
        session.getTransaction().begin();
    }

    @Test
    void get_task_by_id() {
        long expId = 3L;
        String expTitle = "Task3";

        Task task = dao.getById(expId, session);

        assertEquals(expTitle, task.getTitle());
        assertNull(task.getDescription());
    }

    @Test
    void save_new_task() {
        expId = LAST_ID_TASKS + 1;
        expTitle = "Task3";
        expPriority = TaskPriority.HIGH;
        expStatus = TaskStatus.IN_PROGRESS;
        expUserId = 2L;

        var task = Task.builder()
                .title(expTitle)
                .priority(expPriority)
                .status(expStatus)
                .userId(expUserId)
                .build();

        Task saved = dao.save(task);

        assertEquals(expId, saved.getId());
        assertEquals(expTitle, saved.getTitle());
        assertEquals(expPriority, saved.getPriority());
        assertEquals(expStatus, saved.getStatus());
        assertEquals(expUserId, saved.getUserId());
    }

    @Test
    void update_task() {
        expId = 5L;
        expTitle = "Task5 updated";
        expPriority = TaskPriority.LOW;
        expStatus = TaskStatus.DONE;
        expUserId = 3L;
        String expDescription = "Task5 description";
        Set<Tag> expTags = new HashSet<>();

        Task task = dao.getById(expId, session);
        task.setTitle(expTitle);
        task.setDescription(expDescription);
        task.setPriority(expPriority);
        task.setStatus(expStatus);
        task.setTags(expTags);

        Task updated = dao.update(task);

        assertEquals(expId, updated.getId());
        assertEquals(expTitle, updated.getTitle());
        assertEquals(expDescription, updated.getDescription());
        assertEquals(expPriority, updated.getPriority());
        assertEquals(expStatus, updated.getStatus());
        assertEquals(expTags, updated.getTags());
        assertEquals(expUserId, updated.getUserId());
    }

    @Test
    void delete_by_id() {
        expId = 4L;
        dao.delete(expId, session);
        assertNull(dao.getById(expId, session));
    }

    @Test
    void get_all_user_tasks() {
        expUserId = 3L;
        String username = "test3";
        int expTasksNumber = 3;

        List<Task> userTasks = dao.getAllUserTasks(username, session);

        assertEquals(expTasksNumber, userTasks.size());
        userTasks.forEach(task -> assertEquals(expUserId, task.getUserId()));
    }
}
