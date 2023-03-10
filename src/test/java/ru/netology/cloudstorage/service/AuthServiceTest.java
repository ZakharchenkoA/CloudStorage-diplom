package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.dto.request.AuthRequest;
import ru.netology.cloudstorage.dto.response.AuthResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        when(userService.getUserAuth()).thenReturn(USER);
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testLoginSuccess() {
        final var authRequest = new AuthRequest(LOGIN, PASSWORD);
        final var expectedAuthResponse = new AuthResponse(TOKEN_ONE);
        when(tokenService.generateToken(USER)).thenReturn(TOKEN_ONE);

        final var actualAuthResponse = authService.login(authRequest);

        Assertions.assertEquals(expectedAuthResponse, actualAuthResponse);
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD));
        verify(tokenService, times(1)).addTokenInStorage(TOKEN_ONE);
    }

    @Test
    public void testLogoutSuccess() {
        authService.logout(VALUE_HEADER_AUTH_TOKEN);

        verify(tokenService, times(1)).removeTokenFromStorage(TOKEN_ONE);
    }
}