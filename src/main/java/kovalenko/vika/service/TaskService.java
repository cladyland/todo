package kovalenko.vika.service;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllUserTasks(String username);
    TaskDTO createTask(TaskCommand taskCommand);
    void updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(Long id);
}
