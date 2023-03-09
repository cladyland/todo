package kovalenko.vika.dao;

import kovalenko.vika.model.Task;
import org.hibernate.Session;

import java.util.List;

public interface TaskDAO {
    Task getById(Long id, Session session);
    Task save(final Task entity);
    Task update(final Task entity);
    Task delete(Long id, Session session);
    List<Task> getAllUserTasks(String username, Session session);
    Session getCurrentSession();
}
