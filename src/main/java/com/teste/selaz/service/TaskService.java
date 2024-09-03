package com.teste.selaz.service;

import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.TaskRepository;
import com.teste.selaz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> listTasks(Long userId) {

        return taskRepository.findTaskByUserId(userId);
    }

    @Transactional
    public TaskDTO createTask(Task task) {
        Task newTask = taskRepository.save(task);
         return new TaskDTO(
                newTask.getId(),
                newTask.getTitle(),
                newTask.getDescription(),
                newTask.getDueDate(),
                newTask.getStatus(),
                newTask.getUser().getId()
        );
    }
//Long id, String title, String description, String dueDate, Status status, Long userID
    @Transactional
    public Task updateTask(Long id, Task task) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
           taskOptional.get().setTitle(task.getTitle());
           taskOptional.get().setDescription(task.getDescription());
           taskOptional.get().setDueDate(task.getDueDate());
           taskOptional.get().setStatus(task.getStatus());
           return taskOptional.get();
        }
        return null;
    }

    public String deleteTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
            return "Task deleted";
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Transactional
    public void deleteTasksByUserID(Long userId) {

        List<Task> taskList = taskRepository.findTaskByUserId(userId);

        if(taskList.isEmpty()) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteAll(taskList);

    }
    @Transactional
    public Task loadTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new EntityNotFoundException("Task not found");
    }

    public List<Task> loadTasksByStatus(Long userID, Status status) {
        List<Task> taskList = listTasks(userID);
        List<Task> taskListByStatus = new ArrayList<>();
        for(Task task: taskList){
            if (task.getStatus().equals(status)){
                taskListByStatus.add(task);
            }
        }
        return taskListByStatus;
    }

    public List<Task> loadTasksByDueDate(Long userID) {
        List<Task> taskList = taskRepository.findAllTasksOrderedByDueDate(userID);

        return taskList;
    }

    public List<Task> loadTasksByUserId(Long userID) {
        return taskRepository.findTaskByUserId(userID);
    }
}
