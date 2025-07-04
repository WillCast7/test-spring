package com.techtest.willcast.service.impl;

import com.techtest.willcast.dto.ApiResponseDTO;
import com.techtest.willcast.dto.request.LoginRequestDTO;
import com.techtest.willcast.dto.response.LoginResponseDTO;
import com.techtest.willcast.dto.response.UserDetailedResponse;
import com.techtest.willcast.dto.response.UserListResponseDTO;
import com.techtest.willcast.dto.response.UserResponseDTO;
import com.techtest.willcast.model.entities.LoginLogEntity;
import com.techtest.willcast.model.repositories.LoginLogRepository;
import com.techtest.willcast.service.UserService;
import com.techtest.willcast.utils.constants;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DummyJSONService dummyJSONService;

    @Autowired
    private LoginLogRepository loginLogRepository;

    /**
     *
     * @param loginCredentials: {user, password}
     *                        This method is the login method for create the session and
     *                        register in the database
     * @return
     */
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(LoginRequestDTO loginCredentials){
        try {
            ResponseEntity<LoginResponseDTO> userLogged = dummyJSONService.login(loginCredentials);

            LoginLogEntity newLog = LoginLogEntity.builder()
                    .username(userLogged.getBody().getUsername())
                    .accessToken(userLogged.getBody().getAccessToken())
                    .refreshToken(userLogged.getBody().getRefreshToken())
                    .build();

            loginLogRepository.save(newLog);

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", userLogged.getBody().getAccessToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofMinutes(80))
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", userLogged.getBody().getRefreshToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofMinutes(80))
                    .build();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            responseHeaders.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return ResponseEntity
                    .ok()
                    .headers(responseHeaders)
                    .body(ApiResponseDTO.success(userLogged.getBody(), constants.success.loginSuccess));

        }catch (FeignException e) {
            System.out.println("Error Feign: " + e.status());
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Body: " + e.contentUTF8());
            return ResponseEntity.ofNullable(ApiResponseDTO.failure(constants.errors.invalidUserOrPass, e.getMessage()));
        }
    }

    /**
     *
     * @param accessToken: token for access at the application
     * @param refreshToken: toker for refresh the session
     *                    This method searches the information of the person with the active session
     * @return
     */
    public ResponseEntity<ApiResponseDTO<UserDetailedResponse>> getMyInformamtion(String accessToken, String refreshToken) {
        try {
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalArgumentException("Token is empty");
            }
            String bearerToken = "Bearer " + accessToken.trim();
            UserDetailedResponse dataObject = dummyJSONService.getMyInfo(bearerToken);
            return ResponseEntity.ok(ApiResponseDTO.success(dataObject, constants.success.findedSuccess));
        } catch (FeignException e) {
            return ResponseEntity.ofNullable(ApiResponseDTO.failure(constants.errors.invalidUserOrPass, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ofNullable(ApiResponseDTO.failure("Error inesperado", e.getMessage()));
        }
    }

    /**
     *
     * @param accessToken: token for access at the application
     * @param refreshToken: toker for refresh the session
     *                    This method searches sll the users of the person with the active session
     * @return
     */
    public ResponseEntity<ApiResponseDTO<UserListResponseDTO>> getAllUsers(String accessToken, String refreshToken){
        try {
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalArgumentException("Token is empty");
            }

            String bearerToken = "Bearer " + accessToken.trim();

            System.out.println("dataObject");
            UserListResponseDTO dataObject = dummyJSONService.getAllUsers(bearerToken);
            System.out.println("dataObject");
            System.out.println(dataObject);
            return ResponseEntity.ok(ApiResponseDTO.success(dataObject, constants.success.findedSuccess));
        } catch (FeignException e) {
            return ResponseEntity.ofNullable(ApiResponseDTO.failure(constants.errors.invalidUserOrPass, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ofNullable(ApiResponseDTO.failure("Error inesperado", e.getMessage()));
        }
    }

}
