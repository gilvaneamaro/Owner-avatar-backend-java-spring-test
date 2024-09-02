package com.teste.selaz.controller;

import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import com.teste.selaz.service.TaskService;
import com.teste.selaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam(value = "status", required = false) Status status,
                                                  @RequestParam(value = "sort", required = false) String sort) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());

        if (sort != null && sort.toUpperCase().equals("DUEDATE")){
            return ResponseEntity.ok().body(taskService.loadTasksByDueDate(userId));
        }

        if(status != null ) {
            return ResponseEntity.ok().body(taskService.loadTasksByStatus(userId, status));
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskService.listTasks(userId));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(id, task));
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteTask(id));
    }
}
