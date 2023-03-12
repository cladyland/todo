package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOImp implements UserDAO {
    private static final Logger LOG = LoggerFactory.getLogger(UserDAOImp.class);
    private final SessionFactory sessionFactory;

    public UserDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        LOG.info("'UserDAOImp' initialized");
    }

    @Override
    public User save(User entity) {
        getCurrentSession().persist(entity);
        LOG.info("User '{}' added to DB", entity.getUsername());
        return entity;
    }

    @Override
    public User update(final User entity) {
        getCurrentSession().merge(entity);
        LOG.info("Data of user '{}' has been updated", entity.getUsername());
        return entity;
    }

    @Override
    public User getUserByUsername(String name, Session session) {
        String queryStr = "select u from User u where u.username = :username";
        Query<User> query = session.createQuery(queryStr, User.class);
        query.setParameter("username", name);
        return query.getSingleResult();
    }

    @Override
    public Long getUserId(String username) {
        String queryStr = "select u.id from User u where u.username = :username";
        Query<Long> query = getCurrentSession().createQuery(queryStr, Long.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}