package com.techtest.willcast.controller;

import com.techtest.willcast.dto.ApiResponseDTO;
import com.techtest.willcast.dto.response.UserDetailedResponse;
import com.techtest.willcast.dto.response.UserListResponseDTO;
import com.techtest.willcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(produces = "application/json", value = "/users")
    ResponseEntity<ApiResponseDTO<UserListResponseDTO>> getallUsers(@CookieValue("accessToken") String accessToken,
                                                                    @CookieValue("refreshToken") String refreshToken){
        return userService.getAllUsers(accessToken, refreshToken);
    }
}
