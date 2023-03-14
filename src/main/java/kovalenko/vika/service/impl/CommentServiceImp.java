package kovalenko.vika.service.impl;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.CommentDTO;
import kovalenko.vika.mapper.CommentMapper;
import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.CommentService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentServiceImp implements CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImp.class);
    private final CommentDAO commentDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
    private final CommentMapper commentMapper;

    public CommentServiceImp(CommentDAO commentDAO, TaskDAO taskDAO, UserDAO userDAO) {
        this.commentDAO = commentDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.commentMapper = CommentMapper.INSTANCE;

        LOG.debug("'CommentServiceImp' initialized");
    }

    @Override
    public CommentDTO createComment(CommentCommand commentCommand) {
        Long taskId = commentCommand.getTaskId();
        String username = commentCommand.getUsername();

        try (Session session = commentDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.getById(taskId, session);
            User user = userDAO.getUserByUsername(username, session);

            Comment comment = commentMapper.mapToEntity(commentCommand);
            comment.setTask(task);
            comment.setUser(user);
            Comment savedComment = commentDAO.save(comment);

            session.getTransaction().commit();
            return commentMapper.mapToDTO(savedComment);
        }
    }
}