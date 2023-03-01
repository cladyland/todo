package kovalenko.vika.service;

import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TaskService {
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public TaskService(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    public List<Task> getAllUserTasks(UserDTO userDTO){
        User user = userDAO.getUserByUsername(userDTO.getUsername());
        return taskDAO.getAllUserTasks(user);
    }

    public void createTask(TaskDTO taskDTO, UserDTO userDTO) {
        Session session = taskDAO.getCurrentSession();
        User user = findUserByUsername(userDTO.getUsername());

        Task task = Task.builder()
                .userId(user)
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .build();

        taskDAO.save(task);
        session.getTransaction().commit();
    }

    public void updateTask() {
    }

    public void deleteTask() {
    }

    private User findUserByUsername(String username){
        return userDAO.getUserByUsername(username);
    }
}
