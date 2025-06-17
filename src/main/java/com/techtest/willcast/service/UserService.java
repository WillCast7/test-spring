package com.techtest.willcast.service;

import com.techtest.willcast.dto.ApiResponseDTO;
import com.techtest.willcast.dto.request.LoginRequestDTO;
import com.techtest.willcast.dto.response.LoginResponseDTO;
import com.techtest.willcast.dto.response.UserDetailedResponse;
import com.techtest.willcast.dto.response.UserListResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface UserService {
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(LoginRequestDTO loginCredentials);
    public ResponseEntity<ApiResponseDTO<UserDetailedResponse>> getMyInformamtion(String authHeader, String refreshToken);
    public ResponseEntity<ApiResponseDTO<UserListResponseDTO>> getAllUsers(String authHeader, String refreshToken);
}
