package com.teste.selaz.controller;

import com.teste.selaz.entity.Users;
import com.teste.selaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    UserService usuarioService;

    @GetMapping
    public ResponseEntity<List<Users>> loadUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listUsers());
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.createUser(user));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) throws Exception {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.updateUser(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.deleteUser(id));
    }
}
