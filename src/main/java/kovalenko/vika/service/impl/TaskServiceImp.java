package kovalenko.vika.service.impl;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.mapper.TaskMapper;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.TaskService;
import org.hibernate.Session;

import java.util.List;

public class TaskServiceImp implements TaskService {
    private final TaskDAO taskDAO;
    private final TaskMapper taskMapper;

    public TaskServiceImp(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
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
    public List<TaskDTO> getAllUserTasks(String username) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            List<TaskDTO> tasks = taskDAO.getAllUserTasks(username, session);
            session.getTransaction().commit();
            return tasks;
        }
    }

    @Override
    public TaskDTO createTask(TaskCommand taskCommand) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskMapper.mapToEntity(taskCommand);
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
}
