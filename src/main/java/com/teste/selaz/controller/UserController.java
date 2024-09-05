package com.teste.selaz.controller;

import com.teste.selaz.dto.*;
import com.teste.selaz.entity.User;
import com.teste.selaz.repository.UserRepository;
import com.teste.selaz.security.TokenService;
import com.teste.selaz.service.TaskService;
import com.teste.selaz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    TokenService tokenService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Buscar todos os usuários cadastrados",
            description = "Retorna um array de objetos User, vazio no caso de não existir usuários",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = UserDTO.class)))                    ),
                    @ApiResponse(responseCode = "401", description = "Token expirado, usuário sem credencial.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @GetMapping
    public ResponseEntity<List<UserDTO>> loadUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.listUsers());
    }

    @Operation(
            summary = "Atualizar dados do usuário.",
            description = "Recebe um User para atualizar nome, email ou role do usuário. Retorna um UserDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "Já existe um outro usuário com o mesmo username.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Token expirado, usuário sem credencial.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    @Operation(
            summary = "Deletar um usuário.",
            description = "Busca e deleta usuário com ID passado pela URL",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Token expirado, usuário sem credencial.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    @Operation(
            summary = "Realizar login.",
            description = "Recebe um usuário e senha, e em caso de sucesso, retorna um token válido por 2 horas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.",
                            content = @Content(mediaType = "application/json")),

            }
    )
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(data));

    }

    @Operation(
            summary = "Criar um novo usuário.",
            description = "Recebe um objeto User, caso não tenha duplicidade no username, retorna um UserDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário cadastro com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Já existe um usuário com o mesmo username.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Token expirado, usuário sem credencial.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @Operation(
            summary = "Buscar todas as tarefas de um usuário.",
            description = "Recebe um ID de usuário pela URL e retorna todas as tasks relacionadas ao mesmo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista encontrada com sucesso.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = TaskDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Token expirado, usuário sem credencial.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @GetMapping(value = "/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> loadTasksByUser(@PathVariable @Valid Long userId) {
        userService.findByID(userId);
        return ResponseEntity.ok().body(taskService.listTasks(userId));
    }
}
