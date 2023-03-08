package kovalenko.vika.service.impl;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import kovalenko.vika.mapper.TagMapper;
import kovalenko.vika.mapper.TaskMapper;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.TaskService;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskServiceImp implements TaskService {
    private final TaskDAO taskDAO;
    private final TagDAO tagDAO;
    private final TaskMapper taskMapper;
    private final TagMapper tagMapper;

    public TaskServiceImp(TaskDAO taskDAO, TagDAO tagDAO) {
        this.taskDAO = taskDAO;
        this.tagDAO = tagDAO;
        this.taskMapper = TaskMapper.INSTANCE;
        this.tagMapper = TagMapper.INSTANCE;
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
            List<Task> tasks = taskDAO.getAllUserTasks(username, session);
            List<TaskDTO> t = tasks
                    .stream()
                    .map(taskMapper::mapToDTO)
                    .collect(Collectors.toList());

            for (int i = 0; i < tasks.size(); i++) {
                List<TagDTO> tags = tasks.get(i).getTags().stream().map(tagMapper::mapToDTO).collect(Collectors.toList());
                t.get(i).setTags(tags);
                t.get(i).setPriority(tasks.get(i).getPriority());
                t.get(i).setStatus(tasks.get(i).getStatus());
            }
            session.getTransaction().commit();
            return t;
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
        try (Session session = taskDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task task = taskMapper.mapToEntity(taskCommand);
            task.setTags(tagDAO.getTagsByIds(tagIds));
            task.setPriority(taskCommand.getPriority());
            task.setStatus(taskCommand.getStatus());
            taskDAO.save(task);
            TaskDTO taskDTO = taskMapper.mapToDTO(task);
            List<TagDTO> tags = task
                    .getTags()
                    .stream()
                    .map(tagMapper::mapToDTO)
                    .collect(Collectors.toList());
            taskDTO.setTags(tags);
            session.getTransaction().commit();
            return taskDTO;
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
