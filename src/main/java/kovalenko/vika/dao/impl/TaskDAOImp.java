package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TaskDAOImp implements TaskDAO {
    private final SessionFactory sessionFactory;

    public TaskDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task getById(Long id, Session session) {
        return session.get(Task.class, id);
    }

    @Override
    public Task save(Task entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public Task update(final Task entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public Task delete(Long id, Session session) {
        Task element = getById(id, session);
        session.remove(element);
        return element;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Task> getAllUserTasks(String username, Session session) {
        String queryStr = "select t from Task t where t.user.username = :user";
        Query<Task> query = session.createQuery(queryStr, Task.class);
        query.setParameter("user", username);
        return query.getResultList();
    }

}
