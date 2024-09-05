package com.teste.selaz.controller;

import com.teste.selaz.dto.TaskCreateDTO;
import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.dto.UserDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Status;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.exception.InvalidDueDateException;
import com.teste.selaz.service.TaskService;
import com.teste.selaz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Buscar todas as tasks do usuário autenticado.",
            description = "Retorna todas as tasks relacionadas ao usuário autenticado recebido pelo token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task encontradas com sucesso.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = TaskDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam(value = "status", required = false) Status status,
                                                  @RequestParam(value = "sort", required = false) String sort) {
                Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok().body(taskService.list(sort, status, userId));
    }

    @Operation(
            summary = "Criar uma nova task.",
            description = "Recebe um objeto Task, caso o id do usuário na task seja válido, retorna um TaskDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task criada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não cadastrado.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Data de vencimento não pode ser antes da data atual.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskCreateDTO task) {
            User user = userService.findUserByID(task.userID());
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task, user));
    }

    @Operation(
            summary = "Atualiza uma nova task.",
            description = "Recebe um objeto Task e o id da task pela URL, atualiza os parâmetros da task e retorna uma TaskDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Usuário/task não cadastrado.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PutMapping(value="/{id}")
    public ResponseEntity<TaskDTO> updateTask(@RequestBody Task task, @PathVariable long id) {

        userService.findByID(task.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(id, task));

    }

    @Operation(
            summary = "Deletar uma task.",
            description = "Recebe o id da task pela URL, caso encontre, a task é deletada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task deletada com sucesso.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Task não encontrada.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @DeleteMapping(value="/{id}")
    public ResponseEntity deleteTask(@PathVariable long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteTask(id));
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
