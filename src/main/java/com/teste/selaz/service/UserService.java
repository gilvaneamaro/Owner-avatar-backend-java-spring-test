package com.teste.selaz.service;

import com.teste.selaz.entity.Users;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<Users> listUsers(){
        return userRepository.findAll();
    }

    public Users createUser(Users user){
        return userRepository.save(user);
    }

    @Transactional
    public Users updateUser(Long id, Users updatedUser) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setUsername(updatedUser.getUsername());
        user.setLevel(updatedUser.getLevel());

        return user;
    }

    @Transactional
    public  String deleteUser(Long id){
        Optional<Users> oldUser = userRepository.findById(id);
        if(oldUser.isPresent()) {
            userRepository.delete(oldUser.get());
            return "User deleted";
        }
        return "User not found";
    }
}
