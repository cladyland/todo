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

    public TaskDTO getTaskById(Long id) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.getById(id, session);
            session.getTransaction().commit();
            return transformToDTO(task);
        }
    }

    public List<TaskDTO> getAllUserTasks(UserDTO userDTO) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User user = userDAO.getUserByUsername(userDTO.getUsername(), session);
            List<Task> tasks = taskDAO.getAllUserTasks(user, session);
            List<TaskDTO> taskDTOS = new ArrayList<>();
            for (Task task : tasks) {
                taskDTOS.add(transformToDTO(task));
            }
            session.getTransaction().commit();
            return taskDTOS;
        }
    }

    public TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO) {
        try(Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User user = findUserByUsername(userDTO.getUsername(), session);

            Task task = Task.builder()
                    .userId(user)
                    .title(taskDTO.getTitle())
                    .description(taskDTO.getDescription())
                    .build();

            taskDAO.save(task);
            session.getTransaction().commit();
            return transformToDTO(task);
        }
    }

    public void updateTask(TaskDTO taskDTO) {
        try(Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskDAO.getById(taskDTO.getId(), session);
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            taskDAO.update(task);

            session.getTransaction().commit();
        }
    }

    public TaskDTO deleteTask(Long id) {
        try(Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.delete(id, session);
            session.getTransaction().commit();
            return transformToDTO(task);
        }
    }

    private TaskDTO transformToDTO(Task task){
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }

    private User findUserByUsername(String username, Session session) {
        return userDAO.getUserByUsername(username, session);
    }
}
