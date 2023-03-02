package kovalenko.vika.service;

import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public TaskService(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    public TaskDTO getTaskById(Long id){
        Session session = userDAO.getCurrentSession();
        session.getTransaction().begin();
        Task task = taskDAO.getById(id, session);
        session.getTransaction().commit();
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }

    public List<TaskDTO> getAllUserTasks(UserDTO userDTO){
        Session session = userDAO.getCurrentSession();
        session.getTransaction().begin();
        User user = userDAO.getUserByUsername(userDTO.getUsername(), session);
        List<Task> tasks = taskDAO.getAllUserTasks(user, session);
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (Task task : tasks){
            taskDTOS.add(TaskDTO.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .build());
        }

        return taskDTOS;
    }

    public void createTask(TaskDTO taskDTO, UserDTO userDTO) {
        Session session = taskDAO.getCurrentSession();
        session.getTransaction().begin();
        User user = findUserByUsername(userDTO.getUsername(), session);

        Task task = Task.builder()
                .userId(user)
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .build();

        taskDAO.save(task);
        session.getTransaction().commit();
        session.close();
    }

    public void updateTask(TaskDTO taskDTO) {
        Session session = taskDAO.getCurrentSession();
        session.getTransaction().begin();
        Task task = taskDAO.getById(taskDTO.getId(), session);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        taskDAO.update(task);
        session.getTransaction().commit();
    }

    public void deleteTask(Long id) {
        Session session = taskDAO.getCurrentSession();
        session.getTransaction().begin();
        taskDAO.delete(id, session);
        session.getTransaction().commit();
        session.close();
    }

    private User findUserByUsername(String username, Session session){
        return userDAO.getUserByUsername(username, session);
    }
}
