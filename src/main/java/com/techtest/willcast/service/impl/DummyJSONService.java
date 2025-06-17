package com.techtest.willcast.service.impl;

import com.techtest.willcast.dto.request.LoginRequestDTO;
import com.techtest.willcast.dto.response.LoginResponseDTO;
import com.techtest.willcast.dto.response.UserDetailedResponse;
import com.techtest.willcast.dto.response.UserListResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Set;

@FeignClient(name = "authClient", url = "https://dummyjson.com")
public interface DummyJSONService {

    @PostMapping(value = "/auth/login", consumes = "application/json")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request);

    @GetMapping(value = "/auth/me", consumes = "application/json")
    UserDetailedResponse getMyInfo(@RequestHeader("Authorization") String cookie);

    @GetMapping(value = "/users", consumes = "application/json")
    UserListResponseDTO getAllUsers(@RequestHeader("Authorization") String cookie);
}
