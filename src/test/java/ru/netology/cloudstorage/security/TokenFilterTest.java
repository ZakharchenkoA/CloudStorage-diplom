package ru.netology.cloudstorage.security;

import ru.netology.cloudstorage.service.TokenService;
import ru.netology.cloudstorage.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenFilterTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private TokenFilter tokenFilter;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testDoFilterInternalSuccess() throws ServletException, IOException {
        when(httpServletRequest.getHeader(HEADER_AUTH_TOKEN)).thenReturn(VALUE_HEADER_AUTH_TOKEN);
        when(tokenService.isTokenInStorage(TOKEN_ONE)).thenReturn(true);
        when(tokenService.checkToken(TOKEN_ONE)).thenReturn(true);
        when(tokenService.getUserIdFromToken(TOKEN_ONE)).thenReturn(ID);
        when(userService.getUser(ID)).thenReturn(USER_ONLY_ID);

        tokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(tokenService, never()).removeTokenFromStorage(TOKEN_ONE);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {VALUE_HEADER_INVALID_ONE, VALUE_HEADER_INVALID_TWO})
    public void testDoFilterInternalTheHeaderValueIsInvalid(String valueHeaderAuthToken) throws ServletException, IOException {
        when(httpServletRequest.getHeader(HEADER_AUTH_TOKEN)).thenReturn(valueHeaderAuthToken);

        tokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(tokenService, never()).removeTokenFromStorage(TOKEN_ONE);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void testDoFilterInternalThereIsNoTokenInTheStorage() throws ServletException, IOException {
        when(httpServletRequest.getHeader(HEADER_AUTH_TOKEN)).thenReturn(VALUE_HEADER_AUTH_TOKEN);
        when(tokenService.isTokenInStorage(TOKEN_ONE)).thenReturn(false);

        tokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(tokenService, never()).removeTokenFromStorage(TOKEN_ONE);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        when(httpServletRequest.getHeader(HEADER_AUTH_TOKEN)).thenReturn(VALUE_HEADER_AUTH_TOKEN);
        when(tokenService.isTokenInStorage(TOKEN_ONE)).thenReturn(true);
        when(tokenService.checkToken(TOKEN_ONE)).thenReturn(false);

        tokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(tokenService, times(1)).removeTokenFromStorage(TOKEN_ONE);
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
    }
}