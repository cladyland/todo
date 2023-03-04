package kovalenko.vika.service;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;

import java.util.List;

public interface TaskService {
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllUserTasks(UserDTO userDTO);
    TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO);
    void updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(Long id);
}
