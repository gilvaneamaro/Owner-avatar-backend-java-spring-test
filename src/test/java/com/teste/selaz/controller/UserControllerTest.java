package com.teste.selaz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.selaz.dto.RegisterDTO;
import com.teste.selaz.dto.UserDTO;
import com.teste.selaz.enums.Role;
import com.teste.selaz.exception.UserAlreadyExistsException;
import com.teste.selaz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc;

    private UserDTO userDTO;

    private RegisterDTO registerDTO;

    private Role role;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
        registerDTO = new RegisterDTO("gilvaneamaro", "123456", Role.ADMIN);
        userDTO = new UserDTO(1L,"gilvaneamaro",Role.ADMIN);
        role = Role.ADMIN;
    }

    @Test
    void mustCreateUserSuccess() throws Exception {
        when(userService.createUser(registerDTO)).thenReturn(userDTO);

        String registerDTOJson = new ObjectMapper().writeValueAsString(registerDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                        .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(userService).createUser(registerDTO);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void mustCreateUserFailsWhenDuplicateUsername() throws Exception {
        when(userService.createUser(registerDTO)).thenReturn(userDTO);

        String registerDTOJson = new ObjectMapper().writeValueAsString(registerDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        doThrow(new UserAlreadyExistsException("Username already exists"))
                .when(userService).createUser(any(RegisterDTO.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        verify(userService, times(2)).createUser(any(RegisterDTO.class));

    }

}
