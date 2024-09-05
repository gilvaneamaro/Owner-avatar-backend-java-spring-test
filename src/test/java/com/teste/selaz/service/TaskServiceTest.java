package com.teste.selaz.service;

import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.entity.Task;
import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Role;
import com.teste.selaz.enums.Status;
import com.teste.selaz.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    private Task task;
    private TaskDTO taskDTO;
    private User user;
    LocalDateTime date;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.of(2024,12,31,23,59);
        user = new User(1L,"gilvaneamaro","123456", Role.ADMIN);
        taskDTO = new TaskDTO(1L,"title","description", date, date, Status.PENDENTE,1L);
        task = new Task (1L, "title", "description", date, date,Status.PENDENTE,user);
    }


}
