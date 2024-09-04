package com.teste.selaz.dto;

import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;

import java.time.LocalDateTime;

public record TaskCreateDTO(String title, String description, LocalDateTime dueDate, Status status, Long userID) {
    public Task toTask (TaskCreateDTO dto){
        return new Task (dto.title, dto.description, dto.dueDate, dto.status);
    }
}
