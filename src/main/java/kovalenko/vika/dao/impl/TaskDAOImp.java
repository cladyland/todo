package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskDAOImp implements TaskDAO {
    private static final Logger LOG = LoggerFactory.getLogger(TaskDAOImp.class);
    private static final String TASK_WITH_ID = "Task with id '{}' has been '{}'";
    private final SessionFactory sessionFactory;

    public TaskDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        LOG.debug("'TaskDAOImp' initialized");
    }

    @Override
    public Task getById(Long id, Session session) {
        return session.get(Task.class, id);
    }

    @Override
    public Task save(Task entity) {
        getCurrentSession().persist(entity);
        LOG.debug(TASK_WITH_ID, entity.getId(), "saved");
        return entity;
    }

    @Override
    public Task update(final Task entity) {
        getCurrentSession().merge(entity);
        LOG.debug(TASK_WITH_ID, entity.getId(), "updated");
        return entity;
    }

    @Override
    public Task delete(Long id, Session session) {
        Task element = getById(id, session);
        session.remove(element);
        LOG.debug(TASK_WITH_ID, id, "removed");
        return element;
    }

    @Override
    public List<Task> getAllUserTasks(String username, Session session) {
        String queryStr = "select t from Task t where t.user.username = :user";
        Query<Task> query = session.createQuery(queryStr, Task.class);
        query.setParameter("user", username);
        return query.getResultList();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
