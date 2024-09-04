package com.teste.selaz.service;

import com.teste.selaz.dto.*;
import com.teste.selaz.entity.Task;
import com.teste.selaz.entity.User;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.exception.UserAlreadyExistsException;
import com.teste.selaz.repository.UserRepository;
import com.teste.selaz.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.util.Assert.notNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDTO createUser(RegisterDTO user) {
        try {
            notNull(user, "User must not be null");

            if (this.userRepository.findByUsername(user.username()) != null)
                throw new UserAlreadyExistsException("Username already exists");

            String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
            User newUser = new User(user.username(), encryptedPassword, user.role());

            userRepository.save(newUser);
            return newUser.toDTO(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public List<UserDTO> listUsers() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            userDTOList.add(user.toDTO(user));
        }
        return userDTOList;
    }
/*
    @Transactional
    public UserDTO updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updatedUser.getUsername() != null)
            user.setUsername(updatedUser.getUsername());
        if (updatedUser.getRole() != null)
            user.setRole(updatedUser.getRole());
        if (updatedUser.getPassword() != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }
        return user.toDTO(user);
    }*/
    @Transactional
    public UserDTO updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(user.getUsername())) {
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getRole() != null && !updatedUser.getRole().equals(user.getRole())) {
            user.setRole(updatedUser.getRole());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }

        userRepository.save(user);
        return user.toDTO(user);
    }

    @Transactional
    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        taskService.deleteTasksByUserID(user.getId());
        userRepository.delete(user);
        return "User deleted";
    }

    public Long findIdByUsername(String username) {
        try {
            notNull(username, "Username cannot be null");
            Long idUser = userRepository.findIdByUsername(username);
            return idUser;
        } catch (final Exception e) {
            throw new EntityNotFoundException("User not found with username: " + username, e);
        }
    }

    public LoginResponseDTO login (AuthenticationDTO data){
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return new LoginResponseDTO(token);
    }

    public UserDTO findByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return user.toDTO(user);
    }

    public User findUserByID(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public UserDTO updatePassword(Long userId, String newPassword) {
        return null;
    }
}
