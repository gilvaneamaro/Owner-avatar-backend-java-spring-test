package com.teste.selaz.service;

import com.teste.selaz.dto.TaskCreateDTO;
import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Status;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.exception.InvalidDueDateException;
import com.teste.selaz.exception.InvalidParamsException;
import com.teste.selaz.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public TaskDTO createTask(TaskCreateDTO task, User user) {
        if (task.dueDate().isBefore(LocalDateTime.now()))
            throw new InvalidDueDateException("Due date cannot be before the current date.");

        Task newTask = task.toTask(task);
        newTask.setUser(user);
        newTask.setCreateAt(LocalDateTime.now());
        taskRepository.save(newTask);

        return newTask.toDTO(newTask);
    }
    @Transactional
    public TaskDTO updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Task not found"));

        if (task.getTitle() != null && !task.getTitle().equals(existingTask.getTitle())) {
            existingTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null && !task.getDescription().equals(existingTask.getDescription())) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.getDueDate() != null && !task.getDueDate().equals(existingTask.getDueDate())) {
            existingTask.setDueDate(task.getDueDate());
        }
        if (task.getStatus() != null && !task.getStatus().equals(existingTask.getStatus())) {
            existingTask.setStatus(task.getStatus());

            return existingTask.toDTO(existingTask);
        }

        return existingTask.toDTO(existingTask);
    }

    public String deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

            taskRepository.delete(task);
            return "Task deleted";
    }

    @Transactional
    public void deleteTasksByUserID(Long userId) {
        List<Task> taskList = taskRepository.findTaskByUserId(userId);
        taskRepository.deleteAll(taskList);

    }

    public List<TaskDTO> loadTasksByStatus(Long userID, Status status) {
        List<Task> taskList = taskRepository.findTaskByUserId(userID);
        List<TaskDTO> taskListByStatus = new ArrayList<>();

        for(Task task: taskList){
            if (task.getStatus().equals(status)){
                taskListByStatus.add(task.toDTO(task));
            }
        }
        return taskListByStatus;
    }

    public List<TaskDTO> loadTasksByDueDate(Long userID) {
        List<TaskDTO> taskListDTO = taskRepository.findAllTasksOrderedByDueDate(userID)
                .stream()
                .map(Task::toDTO)
                .collect(Collectors.toList());

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

    public List<TaskDTO> list(String sort, Status status, Long userID){
        if(sort == null && status == null){
            return listTasks(userID);
        }
        if(sort != null && sort.equalsIgnoreCase("duedate")){
            return loadTasksByDueDate(userID);
        }
        if(status.equals(Status.CONCLUIDA) || status.equals(Status.PENDENTE) || status.equals(Status.EM_ANDAMENTO)){
            return loadTasksByStatus(userID, status);
        }
        throw new InvalidParamsException("Invalid parameters.");
    }
}
