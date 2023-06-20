package kovalenko.vika.service.impl;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.mapper.CommentMapper;
import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

@Slf4j
public class CommentServiceImp implements CommentService {
    private final CommentDAO commentDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
    private final CommentMapper commentMapper;

    public CommentServiceImp(CommentDAO commentDAO, TaskDAO taskDAO, UserDAO userDAO) {
        this.commentDAO = commentDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.commentMapper = CommentMapper.INSTANCE;

        log.debug("'CommentServiceImp' initialized");
    }

    @Override
    public void createComment(CommentCommand commentCommand) {
        Long taskId = commentCommand.getTaskId();
        String username = commentCommand.getUsername();

        try (Session session = commentDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.getById(taskId, session);
            User user = userDAO.getUserByUsername(username, session);

            Comment comment = commentMapper.mapToEntity(commentCommand);
            comment.setTask(task);
            comment.setUser(user);
            commentDAO.save(comment);

            session.getTransaction().commit();
            log.debug("Comment '{}' saved", comment.getId());
        }
    }
}
