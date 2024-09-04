package com.teste.selaz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.selaz.dto.RegisterDTO;
import com.teste.selaz.dto.UserDTO;
import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Role;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.exception.UserAlreadyExistsException;
import com.teste.selaz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private UserDTO userDTO;

    private RegisterDTO registerDTO;

    private User user;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        // Configura os objetos usados nos testes
        registerDTO = new RegisterDTO("gilvaneamaro", "123456", Role.ADMIN);
        // Configura um User com um id fictício e dados necessários
        user = new User("gilvaneamaro", new BCryptPasswordEncoder().encode("123456"), Role.ADMIN);
        user.setId(1L);  // Certifica-se de que o User tem um ID
        userDTO = new UserDTO(1L, "gilvaneamaro", Role.ADMIN);

    }

    @Test
    void mustFindUserIdByUsername(){
        when(userRepository.findIdByUsername(user.getUsername())).thenReturn(user.getId());
        Long expectedId = userService.findIdByUsername(user.getUsername());

        assertEquals(user.getId(), expectedId);
        verify(userRepository).findIdByUsername(user.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void mustNotCallRepositoryWhenUserNotFound(){
        final EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () ->{
            userService.findIdByUsername(null);
        });

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("User not found with username: null"));
        assertThat(e.getCause().getMessage(), is("Username cannot be null"));
        verifyNoInteractions(userRepository);
    }
    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.findByUsername(registerDTO.username())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(registerDTO);

        assertNotNull(result);
        assertEquals("gilvaneamaro", result.username());
        assertEquals(Role.ADMIN, result.role());

        verify(userRepository).findByUsername(registerDTO.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(registerDTO.username())).thenReturn(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(registerDTO);
        });

        assertEquals("Error creating user", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}
