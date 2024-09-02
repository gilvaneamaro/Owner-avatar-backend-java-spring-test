package com.teste.selaz.dto;

import com.teste.selaz.entity.User;
import com.teste.selaz.enums.Role;

public record RegisterDTO(String username, String password, Role role) {
}
