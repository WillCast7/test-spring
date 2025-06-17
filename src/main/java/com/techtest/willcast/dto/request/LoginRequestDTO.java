package com.techtest.willcast.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    public String username;

    @NotBlank(message = "La contraseña es obligatoria")
    public String password;
}
