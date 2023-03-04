package kovalenko.vika.dao;

import kovalenko.vika.dto.TaskDTO;
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

    public List<TaskDTO> getAllUserTasks(User user, Session session){
        String queryStr = "select new kovalenko.vika.dto.TaskDTO(t.id, t.title, t.description) " +
                "from Task t where t.userId = :user";

        Query<TaskDTO> query = session.createQuery(queryStr, TaskDTO.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}
