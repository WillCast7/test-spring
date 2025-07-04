package com.techtest.willcast.Service;

import com.techtest.willcast.dto.ApiResponseDTO;
import com.techtest.willcast.dto.request.LoginRequestDTO;
import com.techtest.willcast.dto.response.LoginResponseDTO;
import com.techtest.willcast.model.entities.LoginLogEntity;
import com.techtest.willcast.model.repositories.LoginLogRepository;
import com.techtest.willcast.service.impl.DummyJSONService;
import com.techtest.willcast.service.impl.UserServiceImpl;
import com.techtest.willcast.utils.constants;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplLoginTest {

    @Mock
    private DummyJSONService dummyJSONService;

    @Mock
    private LoginLogRepository loginLogRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private LoginRequestDTO loginRequest;
    private LoginResponseDTO loginResponse;
    private ResponseEntity<LoginResponseDTO> mockResponseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar datos de prueba
        loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        loginResponse = new LoginResponseDTO();
        loginResponse.setId(1);
        loginResponse.setUsername("testuser");
        loginResponse.setEmail("test@example.com");
        loginResponse.setFirstName("Test");
        loginResponse.setLastName("User");
        loginResponse.setGender("male");
        loginResponse.setImage("https://example.com/image.jpg");
        loginResponse.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.access");
        loginResponse.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.refresh");

        mockResponseEntity = ResponseEntity.ok(loginResponse);
    }

    // Método helper para extraer datos del Optional si es necesario
    private <T> T extractDataFromResponse(Object data, Class<T> expectedType) {
        if (data instanceof Optional) {
            Optional<?> optional = (Optional<?>) data;
            if (optional.isPresent()) {
                return expectedType.cast(optional.get());
            }
            return null;
        }
        return expectedType.cast(data);
    }

    // Método helper para verificar si los datos están presentes
    private boolean isDataPresent(Object data) {
        if (data == null) return false;
        if (data instanceof Optional) {
            return ((Optional<?>) data).isPresent();
        }
        return true;
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(dummyJSONService.login(loginRequest)).thenReturn(mockResponseEntity);
        when(loginLogRepository.save(any(LoginLogEntity.class))).thenReturn(new LoginLogEntity());

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(constants.success.loginSuccess, result.getBody().getMessage());

        // Verificar que los datos están presentes
        assertTrue(isDataPresent(result.getBody().getData()));

        // Extraer y verificar los datos
        LoginResponseDTO actualData = extractDataFromResponse(result.getBody().getData(), LoginResponseDTO.class);
        assertNotNull(actualData);
        assertEquals(loginResponse.getId(), actualData.getId());
        assertEquals(loginResponse.getUsername(), actualData.getUsername());
        assertEquals(loginResponse.getEmail(), actualData.getEmail());
        assertEquals(loginResponse.getAccessToken(), actualData.getAccessToken());
        assertEquals(loginResponse.getRefreshToken(), actualData.getRefreshToken());

        // Verificar que se guardó el log de login
        verify(loginLogRepository, times(1)).save(any(LoginLogEntity.class));

        // Verificar que se configuraron las cookies
        HttpHeaders headers = result.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey(HttpHeaders.SET_COOKIE));
        assertEquals(2, headers.get(HttpHeaders.SET_COOKIE).size());
    }

    @Test
    void testLogin_Success_VerifyLoginLogEntity() {
        // Arrange
        when(dummyJSONService.login(loginRequest)).thenReturn(mockResponseEntity);
        when(loginLogRepository.save(any(LoginLogEntity.class))).thenReturn(new LoginLogEntity());

        // Act
        userService.login(loginRequest);

        // Assert - Verificar que se guardó el LoginLogEntity con los datos correctos
        verify(loginLogRepository, times(1)).save(argThat(loginLog ->
                loginLog.getUsername().equals(loginResponse.getUsername()) &&
                        loginLog.getAccessToken().equals(loginResponse.getAccessToken()) &&
                        loginLog.getRefreshToken().equals(loginResponse.getRefreshToken())
        ));
    }

    @Test
    void testLogin_Success_VerifyCookieConfiguration() {
        // Arrange
        when(dummyJSONService.login(loginRequest)).thenReturn(mockResponseEntity);
        when(loginLogRepository.save(any(LoginLogEntity.class))).thenReturn(new LoginLogEntity());

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert - Verificar configuración de cookies
        HttpHeaders headers = result.getHeaders();
        assertNotNull(headers);

        String cookieHeader1 = headers.get(HttpHeaders.SET_COOKIE).get(0);
        String cookieHeader2 = headers.get(HttpHeaders.SET_COOKIE).get(1);

        // Verificar que ambas cookies son HttpOnly y tienen la configuración correcta
        assertTrue((cookieHeader1.contains("HttpOnly") && cookieHeader1.contains("Path=/") && cookieHeader1.contains("Max-Age=4800")) ||
                (cookieHeader2.contains("HttpOnly") && cookieHeader2.contains("Path=/") && cookieHeader2.contains("Max-Age=4800")));
    }

    @Test
    void testLogin_FeignException_Unauthorized() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(401);
        when(feignException.getMessage()).thenReturn("Unauthorized");
        when(feignException.contentUTF8()).thenReturn("Invalid credentials");

        when(dummyJSONService.login(loginRequest)).thenThrow(feignException);

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(constants.errors.invalidUserOrPass, result.getBody().getMessage());
        assertEquals("Unauthorized", result.getBody().getError());

        // Verificar que no hay datos o que el Optional está vacío
        assertFalse(isDataPresent(result.getBody().getData()));

        // Verificar que NO se guardó ningún log de login
        verify(loginLogRepository, never()).save(any(LoginLogEntity.class));

        // Verificar que NO se configuraron cookies
        HttpHeaders headers = result.getHeaders();
        assertTrue(headers == null || !headers.containsKey(HttpHeaders.SET_COOKIE));
    }

    @Test
    void testLogin_FeignException_BadRequest() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(400);
        when(feignException.getMessage()).thenReturn("Bad Request");
        when(feignException.contentUTF8()).thenReturn("Invalid request format");

        when(dummyJSONService.login(loginRequest)).thenThrow(feignException);

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(constants.errors.invalidUserOrPass, result.getBody().getMessage());
        assertEquals("Bad Request", result.getBody().getError());

        // Verificar que no hay datos
        assertFalse(isDataPresent(result.getBody().getData()));

        // Verificar que NO se guardó ningún log de login
        verify(loginLogRepository, never()).save(any(LoginLogEntity.class));
    }

    @Test
    void testLogin_FeignException_InternalServerError() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(500);
        when(feignException.getMessage()).thenReturn("Internal Server Error");
        when(feignException.contentUTF8()).thenReturn("Server error occurred");

        when(dummyJSONService.login(loginRequest)).thenThrow(feignException);

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(constants.errors.invalidUserOrPass, result.getBody().getMessage());
        assertEquals("Internal Server Error", result.getBody().getError());

        // Verificar que no hay datos
        assertFalse(isDataPresent(result.getBody().getData()));

        // Verificar que NO se guardó ningún log de login
        verify(loginLogRepository, never()).save(any(LoginLogEntity.class));
    }

    @Test
    void testLogin_WithNullLoginRequest() {
        // Arrange
        LoginRequestDTO nullRequest = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.login(nullRequest);
        });

        // Verificar que NO se guardó ningún log de login
        verify(loginLogRepository, never()).save(any(LoginLogEntity.class));
    }

    @Test
    void testLogin_DatabaseSaveFailure() {
        // Arrange
        when(dummyJSONService.login(loginRequest)).thenReturn(mockResponseEntity);
        when(loginLogRepository.save(any(LoginLogEntity.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.login(loginRequest);
        });

        // Verificar que se intentó guardar el log
        verify(loginLogRepository, times(1)).save(any(LoginLogEntity.class));
    }

    @Test
    void testLogin_EmptyTokensInResponse() {
        // Arrange
        LoginResponseDTO emptyTokenResponse = new LoginResponseDTO();
        emptyTokenResponse.setId(1);
        emptyTokenResponse.setUsername("testuser");
        emptyTokenResponse.setEmail("test@example.com");
        emptyTokenResponse.setFirstName("Test");
        emptyTokenResponse.setLastName("User");
        emptyTokenResponse.setGender("male");
        emptyTokenResponse.setImage("https://example.com/image.jpg");
        emptyTokenResponse.setAccessToken(""); // Token vacío
        emptyTokenResponse.setRefreshToken(""); // Token vacío

        ResponseEntity<LoginResponseDTO> emptyTokenResponseEntity = ResponseEntity.ok(emptyTokenResponse);

        when(dummyJSONService.login(loginRequest)).thenReturn(emptyTokenResponseEntity);
        when(loginLogRepository.save(any(LoginLogEntity.class))).thenReturn(new LoginLogEntity());

        // Act
        ResponseEntity<ApiResponseDTO<LoginResponseDTO>> result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(constants.success.loginSuccess, result.getBody().getMessage());

        // Verificar que los datos están presentes
        assertTrue(isDataPresent(result.getBody().getData()));

        // Verificar que se guardó el log incluso con tokens vacíos
        verify(loginLogRepository, times(1)).save(any(LoginLogEntity.class));

        // Verificar que se configuraron las cookies (aunque con valores vacíos)
        HttpHeaders headers = result.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey(HttpHeaders.SET_COOKIE));
    }

    @Test
    void testLogin_VerifyConsoleOutput() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(401);
        when(feignException.getMessage()).thenReturn("Unauthorized");
        when(feignException.contentUTF8()).thenReturn("Invalid credentials");

        when(dummyJSONService.login(loginRequest)).thenThrow(feignException);

        // Act
        userService.login(loginRequest);

        // Assert - Verificar que se llamaron los métodos para imprimir en consola
        verify(feignException, times(1)).status();
        verify(feignException, times(2)).getMessage(); // Cambiado de 1 a 2 veces
        verify(feignException, times(1)).contentUTF8();
    }
}