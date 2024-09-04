package com.teste.selaz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.selaz.dto.RegisterDTO;
import com.teste.selaz.dto.UserDTO;
import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Role;
import com.teste.selaz.exception.UserAlreadyExistsException;
import com.teste.selaz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc, secureMockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private UserDTO userDTO;

    private RegisterDTO registerDTO;

    private Role role;

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
        secureMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        registerDTO = new RegisterDTO("gilvaneamaro", "123456", Role.ADMIN);
        userDTO = new UserDTO(1L,"gilvaneamaro",Role.ADMIN);
        role = Role.ADMIN;

        user = new User("gilvaneamaro","123456",role);
    }

    @Test
    void shouldReturnNotFoundForUnregistredUsers() throws Exception {
        secureMockMvc.perform(post("/api/users/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatedUnregistredUsers() throws Exception {
        String userJson = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUserSuccess() throws Exception {
        when(userService.createUser(registerDTO)).thenReturn(userDTO);

        String registerDTOJson = new ObjectMapper().writeValueAsString(registerDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                .andExpect(status().isCreated());

        verify(userService).createUser(registerDTO);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldFailsWhenCreateUserDuplicateUsername() throws Exception {
        when(userService.createUser(registerDTO)).thenReturn(userDTO);

        String registerDTOJson = new ObjectMapper().writeValueAsString(registerDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                .andExpect(status().isCreated());

        doThrow(new UserAlreadyExistsException("Username already exists"))
                .when(userService).createUser(any(RegisterDTO.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                .andExpect(status().isConflict());

        verify(userService, times(2)).createUser(any(RegisterDTO.class));

    }

    @Test
    void shouldReturnSucessLogin(){

    }

}
