package com.teste.selaz.dto;

import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Status;

import java.time.LocalDateTime;

public record TaskDTO (Long id, String title, String description, LocalDateTime dueDate, Status status, Long userID) {
}