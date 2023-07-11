package kovalenko.vika.service.impl;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.Verifier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImpTest {
    CommentServiceImp service;
    @Mock
    CommentDAO commentDAO;
    @Mock
    TaskDAO taskDAO;
    @Mock
    UserDAO userDAO;
    @Mock
    Session session;

    @BeforeEach
    void init() {
        service = new CommentServiceImp(commentDAO, taskDAO, userDAO);
    }

    @Test
    void create_comment() {
        var comment = CommentCommand.builder()
                .username("test")
                .taskId(1L)
                .contents("comment")
                .build();

        when(commentDAO.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(Mockito.mock(Transaction.class));
        when(taskDAO.getById(1L, session)).thenReturn(Mockito.mock(Task.class));
        when(userDAO.getUserByUsername("test", session)).thenReturn(Mockito.mock(User.class));

        service.createComment(comment);

        Verifier.transactionBegins(session);
        verify(commentDAO, times(1)).save(any(Comment.class));
        Verifier.transactionCommitted(session);
    }
}
