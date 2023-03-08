package kovalenko.vika.service;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TaskDTO;

import java.util.List;
import java.util.Set;

public interface TaskService {
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllUserTasks(String username);
    TaskDTO createTask(TaskCommand taskCommand, Set<Long> tagIds);
    void updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(Long id);
}
