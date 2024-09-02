package com.teste.selaz.dto;

import com.teste.selaz.enums.Role;

public record UserDTO(Long id, String username, Role role) {
}
