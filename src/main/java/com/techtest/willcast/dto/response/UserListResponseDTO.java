package com.techtest.willcast.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Autor: William Castaño ;)
 * Fecha: 17/06/2025
 * Descripción:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDTO {
    private Set<UserDetailedResponse> users;
}
