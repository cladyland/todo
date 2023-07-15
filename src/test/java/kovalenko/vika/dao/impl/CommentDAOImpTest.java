package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.AbstractDAOTest;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static kovalenko.vika.dao.H2Env.LAST_ID_TASKS;
import static kovalenko.vika.dao.H2Env.LAST_ID_USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentDAOImpTest extends AbstractDAOTest {
    private final User USER = buildUser();
    private final Task TASK = buildTask();
    CommentDAOImp dao;

    @Override
    @BeforeEach
    protected void init() {
        dao = new CommentDAOImp(factory);
        session.getTransaction().begin();
    }

    @Test
    void save_first_comment() {
        long expId = 1L;
        String expContent = "first comment test";
        Comment comment = buildComment(expContent);

        Comment actual = dao.save(comment);

        assertEquals(expId, actual.getId());
        assertEquals(expContent, actual.getContents());
        assertEquals(USER, actual.getUser());
        assertNotNull(actual.getCreateDate());
    }

    @Test
    void save_several_comments() {
        long expId1 = 1L;
        long expId2 = 2L;
        long expId3 = 3L;
        String expContent1 = "several comment test 1";
        String expContent2 = "several comment test 2";
        String expContent3 = "several comment test 3";

        Comment actual1 = dao.save(buildComment(expContent1));
        Comment actual2 = dao.save(buildComment(expContent2));
        Comment actual3 = dao.save(buildComment(expContent3));

        assertEquals(expId1, actual1.getId());
        assertEquals(expContent1, actual1.getContents());

        assertEquals(expId2, actual2.getId());
        assertEquals(expContent2, actual2.getContents());

        assertEquals(expId3, actual3.getId());
        assertEquals(expContent3, actual3.getContents());
    }

    private Comment buildComment(String content) {
        return Comment.builder()
                .contents(content)
                .user(USER)
                .task(TASK)
                .build();
    }

    private Task buildTask() {
        return Task.builder()
                .id(LAST_ID_TASKS)
                .userId(LAST_ID_USERS)
                .title("title")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.SUSPENDED)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(LAST_ID_USERS)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .passwordHash("passwordHash")
                .build();
    }
}
