package com.teste.selaz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.selaz.dto.AuthenticationDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private User unRegisteredUser;

    private AuthenticationDTO authenticationDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
        secureMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        role = Role.ADMIN;

        registerDTO = new RegisterDTO("gilvaneamaro", "123456",role);
        userDTO = new UserDTO(1L,"gilvaneamaro",Role.ADMIN);

        user = new User("gilvaneamaro", new BCryptPasswordEncoder().encode("123456"), Role.ADMIN);
        User unRegisteredUser = new User("gilvane","123",role);

         authenticationDTO = new AuthenticationDTO("gilvane","123");



    }

    @Test
    void shouldReturnUnauthorizedForUnregistredUsers() throws Exception {
        String userJson = new ObjectMapper().writeValueAsString(authenticationDTO);

        secureMockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatedUnregistredUsers() throws Exception {
        String userJson = new ObjectMapper().writeValueAsString(unRegisteredUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnSucessLogin(){


    }

}
