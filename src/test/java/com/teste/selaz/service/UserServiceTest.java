package com.teste.selaz.service;

import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Role;
import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp(){
        user = new User(1L, "gilvaneamaro","123456", Role.ADMIN);
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
    void mustCallExceptionWhenRepositoryThrowsException() {
        // when(repository.findPessoa(pessoa.getCpf())).thenThrow(new RuntimeException("Falha ao buscar pessoas por cpf!"));
        when(userRepository.findById(user.getId())).thenThrow(new RuntimeException("User search failed."));


        final EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> {
            userService.findIdByUsername(user.getUsername());
        });

        assertThat(e.getMessage(), is(format("User search failed.", user.getId())));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("User search failed."));
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }
}
