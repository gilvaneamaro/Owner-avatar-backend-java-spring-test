package com.teste.selaz.entity;

import com.teste.selaz.dto.TaskDTO;
import com.teste.selaz.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime dueDate;
    private Status status;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    public Task(String title, String description, LocalDateTime dueDate, Status status) {
        this.title = title;
        this.description = description;
        this.createAt = LocalDateTime.now();
        this.dueDate = dueDate;
        this.status = status;
    }
    public TaskDTO toDTO(Task task) {
        return new TaskDTO( task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getDueDate(),
                            task.getStatus(),
                            task.getUser().getId());
    }
}
