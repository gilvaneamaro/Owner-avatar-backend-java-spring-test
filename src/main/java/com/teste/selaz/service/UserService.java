package com.teste.selaz.service;

import com.teste.selaz.dto.RegisterDTO;
import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.dto.UserDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.entity.User;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.exception.UserAlreadyExistsException;
import com.teste.selaz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.util.Assert.notNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

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
    }


    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            taskService.deleteTasksByUserID(user.get().getId());
            userRepository.delete(user.get());
        }
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
}
