package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class CommentDAOImp implements CommentDAO {
    private final SessionFactory sessionFactory;

    public CommentDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("'CommentDAOImp' initialized");
    }

    @Override
    public Comment save(Comment entity) {
        getCurrentSession().persist(entity);
        log.debug("Comment with id '{}' from user '{}' saved", entity.getId(), entity.getUser().getId());
        return entity;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
