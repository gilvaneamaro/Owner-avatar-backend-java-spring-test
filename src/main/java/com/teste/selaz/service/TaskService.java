package com.teste.selaz.service;

import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.TaskRepository;
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

    @Transactional
    public TaskDTO createTask(Task task) {
        try {
            Task newTask = taskRepository.save(task);

            return task.toDTO(task);
        }
        catch (Exception e) {
            throw new EntityNotFoundException("Error creating task");
        }
    }
    @Transactional
    public TaskDTO updateTask(Long id, Task task) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();

            if (task.getTitle() != null) {
                existingTask.setTitle(task.getTitle());
            }
            if (task.getDescription() != null) {
                existingTask.setDescription(task.getDescription());
            }
            if (task.getDueDate() != null) {
                existingTask.setDueDate(task.getDueDate());
            }
            if (task.getStatus() != null) {
                existingTask.setStatus(task.getStatus());
            }
            return existingTask.toDTO(existingTask);
        }
        throw new EntityNotFoundException("Task with id " + id + " not found.");
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
        taskRepository.deleteAll(taskList);

    }

    public List<TaskDTO> loadTasksByStatus(Long userID, Status status) {
        List<TaskDTO> taskList = listTasks(userID);
        List<TaskDTO> taskListByStatus = new ArrayList<>();

        for(TaskDTO task: taskList){
            if (task.status().equals(status)){
                taskListByStatus.add(task);
            }
        }
        return taskListByStatus;
    }

    public List<TaskDTO> loadTasksByDueDate(Long userID) {
        List<TaskDTO> taskListDTO = new ArrayList<>();
        List<Task> taskList = taskRepository.findAllTasksOrderedByDueDate(userID);

       for(Task task: taskList){
            taskListDTO.add(task.toDTO(task));
        }
        return taskListDTO;
    }

    public List<TaskDTO> listTasks(Long userID) {

        List<Task> taskList = taskRepository.findTaskByUserId(userID);


        List<TaskDTO> taskDTOList = new ArrayList<>();
        for(Task task: taskList){
            taskDTOList.add( task.toDTO(task) );
        }
        return taskDTOList;
    }
}
