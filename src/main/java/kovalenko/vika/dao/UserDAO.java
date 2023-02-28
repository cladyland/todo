package kovalenko.vika.dao;

import kovalenko.vika.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public User getUserByUsername(String name){
        String queryStr = "select u from User u where u.username = :username";
        Query<User> query = getCurrentSession().createQuery(queryStr, User.class);
        query.setParameter("username", name);
        return query.getSingleResult();
    }
}
