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
public class UserController {
    @Autowired
    UserService usuarioService;



    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Buscar todos os usuários cadastrados",
            description = "Retorna um array de objetos User, vazio no caso de não existir usuários",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = UserDTO.class))))
            }
    )
    @GetMapping
    public ResponseEntity<List<UserDTO>> loadUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listUsers());
    }

    @Operation(
            summary = "Atualizar dados do usuário.",
            description = "Recebe um User para atualizar nome, email ou role do usuário. Retorna um UserDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.updateUser(id, user));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Deletar um usuário.",
            description = "Busca e deleta usuário com ID passado pela URL",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            usuarioService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.login(data));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Criar um novo usuário.",
            description = "Recebe um objeto User, caso não tenha duplicidade no username, retorna um UserDTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário cadastro com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Já existe um usuário com o mesmo username.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUser(user));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @Operation(
            summary = "Buscar todas as tarefas de um usuário.",
            description = "Recebe um ID de usuário pela URL e retorna todas as tasks relacionadas ao mesmo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista encontrada com sucesso.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema( schema = @Schema(implementation = TaskDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json")),
            }
    )
    @GetMapping(value = "/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> loadTasksByUser(@PathVariable Long userId) {
        try {
            UserDTO userDTO = userService.findByID(userId);
            return ResponseEntity.ok().body(taskService.listTasks(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
