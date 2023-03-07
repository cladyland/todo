package kovalenko.vika.dao;

import kovalenko.vika.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public User getUserByUsername(String name, Session session) {
        String queryStr = "select u from User u where u.username = :username";
        Query<User> query = session.createQuery(queryStr, User.class);
        query.setParameter("username", name);
        return query.getSingleResult();
    }

    public Long getUserId(String username) {
        String queryStr = "select u.id from User u where u.username = :username";
        Query<Long> query = getCurrentSession().createQuery(queryStr, Long.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}
