package com.teste.selaz.controller;

import com.teste.selaz.dto.*;
import com.teste.selaz.entity.User;
import com.teste.selaz.repository.UserRepository;
import com.teste.selaz.security.TokenService;
import com.teste.selaz.service.TaskService;
import com.teste.selaz.service.UserService;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> loadUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listUsers());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.updateUser(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        usuarioService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUser(user));
    }

    @GetMapping(value = "/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> loadTasksByUser(@PathVariable Long userId){
        return ResponseEntity.ok().body(taskService.listTasks(userId));
    }
}
