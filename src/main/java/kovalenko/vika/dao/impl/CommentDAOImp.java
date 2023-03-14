package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.model.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDAOImp implements CommentDAO {
    private static final Logger LOG = LoggerFactory.getLogger(CommentDAOImp.class);
    private final SessionFactory sessionFactory;

    public CommentDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        LOG.debug("'CommentDAOImp' initialized");
    }

    @Override
    public Comment save(Comment entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
