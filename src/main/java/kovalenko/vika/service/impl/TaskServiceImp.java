package kovalenko.vika.service.impl;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.mapper.TaskMapper;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.utils.AppMiddleware;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
public class TaskServiceImp implements TaskService {
    private final TaskDAO taskDAO;
    private final TagDAO tagDAO;
    private final TaskMapper taskMapper;

    public TaskServiceImp(TaskDAO taskDAO, TagDAO tagDAO) {
        this.taskDAO = taskDAO;
        this.tagDAO = tagDAO;
        this.taskMapper = TaskMapper.INSTANCE;

        log.debug("'TaskServiceImp' initialized");
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskDAO.getById(id, session);
            if (isNull(task)) {
                log.warn("Task with id '{}' is not found", id);
                return null;
            }

            session.getTransaction().commit();
            return taskMapper.mapToDTO(task);
        }
    }

    @Override
    public List<TaskDTO> getAllUserTasks(String username) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            List<Task> tasks = taskDAO.getAllUserTasks(username, session);

            session.getTransaction().commit();
            return taskMapper.listToDTO(tasks);
        }
    }

    @Override
    public List<TaskPriority> getPriorities() {
        return TaskPriority.getAllPriorities();
    }

    @Override
    public List<TaskStatus> getStatuses() {
        return TaskStatus.getAllStatuses();
    }

    @Override
    public TaskDTO createTask(TaskCommand taskCommand, Set<Long> tagIds) {
        AppMiddleware.checkIfTitleIsBlank(taskCommand.getTitle());

        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskMapper.mapToEntity(taskCommand);
            task.setTags(tagDAO.getTagsByIds(tagIds));
            taskDAO.save(task);

            session.getTransaction().commit();
            log.debug("Task '{}' saved", task.getId());

            return taskMapper.mapToDTO(task);
        }
    }

    @Override
    public void updateTask(TaskDTO taskDTO, Set<Long> tagIds) {
        AppMiddleware.checkIfTitleIsBlank(taskDTO.getTitle());

        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskDAO.getById(taskDTO.getId(), session);
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(TaskStatus.getStatusByName(taskDTO.getStatus()));
            task.setPriority(TaskPriority.getPriorityByName(taskDTO.getPriority()));
            task.setTags(tagDAO.getTagsByIds(tagIds));

            taskDAO.update(task);

            session.getTransaction().commit();
            log.debug("Task '{}' updated", task.getId());
        }
    }

    @Override
    public TaskDTO deleteTask(Long id) {
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Task task = taskDAO.delete(id, session);

            session.getTransaction().commit();
            log.debug("Task '{}' deleted", task.getId());

            return taskMapper.mapToDTO(task);
        }
    }
}
