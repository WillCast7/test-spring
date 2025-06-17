package com.techtest.willcast.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
    private String token;
}
