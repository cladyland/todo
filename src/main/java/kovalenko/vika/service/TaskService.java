package kovalenko.vika.service;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;

import java.util.List;
import java.util.Set;

public interface TaskService {
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllUserTasks(String username);
    List<TaskPriority> getPriorities();
    List<TaskStatus> getStatuses();
    TaskDTO createTask(TaskCommand taskCommand, Set<Long> tagIds);
    void updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(Long id);
}
