package com.teste.selaz.controller;

import com.teste.selaz.dto.AuthenticationDTO;
import com.teste.selaz.dto.LoginResponseDTO;
import com.teste.selaz.dto.RegisterDTO;
import com.teste.selaz.dto.UserDTO;
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
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.updateUser(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.deleteUser(id));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUser(user));
    }

    @GetMapping(value = "/{userId}/tasks")
    public ResponseEntity loadTasksByUser(@PathVariable Long userId){
        return ResponseEntity.ok().body(taskService.loadTasksByUserId(userId));
    }
}
