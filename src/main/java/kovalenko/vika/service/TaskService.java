package kovalenko.vika.service;

import kovalenko.vika.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllUserTasks(String username);
    TaskDTO createTask(TaskDTO taskDTO, Long userId);
    void updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(Long id);
}
