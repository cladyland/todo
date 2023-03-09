package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.model.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CommentDAOImp implements CommentDAO {
    private final SessionFactory sessionFactory;

    public CommentDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment getById(Long id, Session session) {
        return session.get(Comment.class, id);
    }

    @Override
    public Comment save(Comment entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public Comment update(final Comment entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public Comment delete(Long id, Session session) {
        Comment element = getById(id, session);
        session.remove(element);
        return element;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
