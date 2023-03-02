package kovalenko.vika.dao;

import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {
    public TaskDAO(SessionFactory sessionFactory) {
        super(Task.class, sessionFactory);
    }

    public List<Task> getAllUserTasks(User user, Session session){
        String queryStr = "select t from Task t where t.userId = :user";
        Query<Task> query = session.createQuery(queryStr, Task.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}
