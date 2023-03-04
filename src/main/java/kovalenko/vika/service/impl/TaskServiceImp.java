package kovalenko.vika.service.impl;

import kovalenko.vika.mapper.TaskMapper;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.TaskService;
import org.hibernate.Session;

import java.util.List;

public class TaskServiceImp implements TaskService {
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
    private final TaskMapper taskMapper;

    public TaskServiceImp(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.taskMapper = TaskMapper.INSTANCE;
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.getById(id, session);
            session.getTransaction().commit();
            return taskMapper.mapToDTO(task);
        }
    }

    @Override
    public List<TaskDTO> getAllUserTasks(UserDTO userDTO) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User user = userDAO.getUserByUsername(userDTO.getUsername(), session);
            List<TaskDTO> tasks = taskDAO.getAllUserTasks(user, session);
            session.getTransaction().commit();
            return tasks;
        }
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User user = findUserByUsername(userDTO.getUsername(), session);

            Task task = taskMapper.mapToEntity(taskDTO);
            task.setUserId(user);

            taskDAO.save(task);
            session.getTransaction().commit();
            return taskMapper.mapToDTO(task);
        }
    }

    @Override
    public void updateTask(TaskDTO taskDTO) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskDAO.getById(taskDTO.getId(), session);
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            taskDAO.update(task);

            session.getTransaction().commit();
        }
    }

    @Override
    public TaskDTO deleteTask(Long id) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskDAO.delete(id, session);
            session.getTransaction().commit();
            return taskMapper.mapToDTO(task);
        }
    }

    private User findUserByUsername(String username, Session session) {
        return userDAO.getUserByUsername(username, session);
    }
}
