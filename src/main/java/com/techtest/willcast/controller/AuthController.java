package com.techtest.willcast.controller;

import com.techtest.willcast.dto.ApiResponseDTO;
import com.techtest.willcast.dto.request.LoginRequestDTO;
import com.techtest.willcast.dto.response.LoginResponseDTO;
import com.techtest.willcast.dto.response.UserDetailedResponse;
import com.techtest.willcast.dto.response.UserResponseDTO;
import com.techtest.willcast.service.UserService;
import feign.Headers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param userRequest
     *      Login Endpoint for expose the api
     * @author: WillCast
     */
    @PostMapping(consumes = "application/json", value = "/login")
    @Headers("Content-Type: application/json")
    ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO userRequest){
        return this.userService.login(userRequest);
    }

    /**
     *
     * @param accessToken: token for access at the application suplied byCookieParam
     * @param refreshToken: toker for refresh the session suplied byCookieParam
     * @return
     */
    @GetMapping(produces = "application/json", value = "/me")
    ResponseEntity<ApiResponseDTO<UserDetailedResponse>> getMyInformamtion(@CookieValue("accessToken") String accessToken,
                                                                           @CookieValue("refreshToken") String refreshToken){
        return userService.getMyInformamtion(accessToken, refreshToken);
    }

}
