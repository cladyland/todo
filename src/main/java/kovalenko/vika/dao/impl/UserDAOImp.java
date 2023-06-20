package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Slf4j
public class UserDAOImp implements UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("'UserDAOImp' initialized");
    }

    @Override
    public User save(User entity) {
        getCurrentSession().persist(entity);
        log.debug("User '{}' added to DB", entity.getUsername());
        return entity;
    }

    @Override
    public User update(final User entity) {
        getCurrentSession().merge(entity);
        log.debug("Data of user '{}' has been updated", entity.getUsername());
        return entity;
    }

    @Override
    public User getUserByUsername(String name, Session session) {
        String queryStr = "select u from User u where u.username = :username";
        Query<User> query = session.createQuery(queryStr, User.class);
        query.setParameter("username", name);

        log.debug("Getting user '{}'", name);

        return query.getSingleResult();
    }

    @Override
    public Long getUserId(String username) {
        String queryStr = "select u.id from User u where u.username = :username";
        Query<Long> query = getCurrentSession().createQuery(queryStr, Long.class);
        query.setParameter("username", username);

        log.debug("Getting userId for user '{}'", username);

        return query.getSingleResult();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
