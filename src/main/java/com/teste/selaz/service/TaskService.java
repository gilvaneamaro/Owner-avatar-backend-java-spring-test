package com.teste.selaz.service;

import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> listTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
           taskOptional.get().setTitle(task.getTitle());
           taskOptional.get().setDescription(task.getDescription());
           taskOptional.get().setDueDate(task.getDueDate());
           taskOptional.get().setStatus(task.getStatus());

           return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
        }
        throw new EntityNotFoundException("Task not found");
    }

    public List<Task> loadTasksByStatus(Status status) {
        return taskRepository.findTaskByStatus(status);
    }

}
