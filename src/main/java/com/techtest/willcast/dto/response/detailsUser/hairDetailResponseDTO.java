package com.techtest.willcast.dto.response.detailsUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Autor: William Castaño ;)
 * Fecha: 17/06/2025
 * Descripción:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class hairDetailResponseDTO {
    private String color;
    private String type;
}
